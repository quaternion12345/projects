#include <stdio.h>
#include <pcap.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <ctype.h>

/* Define structure of Ethernet, IP, TCP, UDP */
#define ETHER_ADDR_LEN 6
#define SIZE_ETHERNET 14	// fixed length
	
/* Ethernet header */
struct sniff_ethernet {
	u_char ether_dhost[ETHER_ADDR_LEN]; // destination host addr
	u_char ether_shost[ETHER_ADDR_LEN]; // source host addr
	u_short ether_type; // ethernet type
};

/* IP header */
struct sniff_ip{
	u_char ip_vhl;	// version <<4, header length >>2
	u_char ip_tos;	// type of service
	u_short ip_len;	// total length
	u_short ip_id;	// identification
	u_short ip_off;	// fragment offset field
#define IP_RF 0x8000	// reserved fragment flag
#define IP_DF 0x4000	// dont fragment flag
#define IP_MF 0x2000	// more fragments flag
#define IP_OFFMASK 0x1fff	// mask for fragmenting bits
	u_char ip_ttl;	// time to live
	u_char ip_p;	// protocol
	u_short ip_sum;	// checksum
	struct in_addr ip_src, ip_dst; // source and destination addr
};
#define IP_HL(ip)	(((ip)->ip_vhl) & 0x0f)

/* TCP header */
struct sniff_tcp {
	u_short th_sport;	// source port
	u_short th_dport;	// destination port
	u_int th_seq;		// sequence number
	u_int th_ack;		// acknowledgement number
	u_char th_offx2;	// data offset
#define TH_OFF(th)	(((th)->th_offx2 & 0xf0) >> 4)
	u_char th_flags;
#define TH_FIN 0x01
#define TH_SYN 0x02
#define TH_RST 0x04
#define TH_PUSH 0x08
#define TH_ACK 0x10
#define TH_URG 0x20
#define TH_ECE 0x40
#define TH_CWR 0x80
#define TH_FLAGS	(TH_FIN|TH_SYN|TH_RST|TH_ACK|TH_URG|TH_ECE|TH_CWR)
	u_short th_win;	// window
	u_short th_sum;	// checksum
	u_short th_urp;	// urgent pointer
};

/* UDP Header */
struct sniff_udp{
	u_short uh_sport;	// source port
	u_short uh_dport;	// destination port
	u_short uh_len;		// total length
	u_short uh_sum;		// checksum	
};

int packet_number = 1; // Order number of packet

void print_HTTP_header(u_char *args, const struct pcap_pkthdr *header, const u_char *packet);
void print_DNS_header(u_char *args, const struct pcap_pkthdr *header, const u_char *packet);
void ctob(const u_char *character);

int main(void){
	pcap_t *handle;	// device handler
	char errbuf[PCAP_ERRBUF_SIZE];	// Error string
	pcap_if_t *alldevs; // for saving Linked List of containing all devices
	pcap_if_t *device;  // device to sniff
	
	/* find all devices and check find process successful */
	if(pcap_findalldevs(&alldevs, errbuf) == -1){
		fprintf(stderr, "Error in function findalldevs: %s\n", errbuf);
		return 2;
	}
	
	/* show list of available devices */
	int number = 1;
	for(device = alldevs; device; device = device->next){
		printf("%d. %s ", number++, device->name);
		if (device->description)
			printf("(%s)\n", device->description);
		else
			printf("(No description available)\n");
	}

	/* Get user input of selecting device */
	int input;
	printf("Enter the device number (1-%d): ", number-1);
	scanf("%d", &input);

	/* Select selected device */
	number = 1;
	for(device = alldevs; device; device = device->next){
		if(number++ >= input)	break;
	}

	/* get network number and mask */
	bpf_u_int32 mask;
	bpf_u_int32 net;
	pcap_lookupnet(device->name, &net, &mask, errbuf);
	
	/* open device */
	handle = pcap_open_live(device->name, BUFSIZ, 1, 1000, errbuf);
	if(handle == NULL){
		fprintf(stderr, "Couldn't open device %s: %s\n", device->name, errbuf);
		return 2;
	}
	pcap_freealldevs(alldevs); // deallocate Linked List

	/* Get user input of selecting protocols */
	int p_input;
	printf("Which header do you want to sniff? Enter the number (HTTP-1, DNS-2): ");
	scanf("%d", &p_input);	
	
	/* Compile and Apply the filter according to user input */
	struct bpf_program fp; // variable for compiled filter

	if(p_input == 1){ // HTTP case
		pcap_compile(handle, &fp, "port 80", 0, net);
		pcap_setfilter(handle, &fp);
		pcap_loop(handle, 0, print_HTTP_header, NULL);
	}
	else if(p_input == 2){ // DNS case
		pcap_compile(handle, &fp, "port 53", 0, net);
		pcap_setfilter(handle, &fp);
		pcap_loop(handle, 0, print_DNS_header, NULL);
	}

	pcap_freecode(&fp);	// deallocate filter program
	pcap_close(handle);	// deallocate device handler

	return 0;
}

// args : pcap_loop function's last parameter
// pcap_pkthdr : struct of packet information about time and size
// packet : pointer of start address of packet

void print_HTTP_header(u_char *args, const struct pcap_pkthdr *header, const u_char *packet){
	/* output form */
	// Number S_IP:S_PORT D_IP:D_PORT HTTP [Request|Response]
	// [Request Line|Status Line]
	// [Header Lines]
	
	// calculate ethernet, ip, tcp, payload start pointer
	const struct sniff_ip *ip = (struct sniff_ip*)(packet + SIZE_ETHERNET);
	int size_ip = IP_HL(ip) * 4;
	const struct sniff_tcp *tcp = (struct sniff_tcp*)(packet + SIZE_ETHERNET + size_ip);
	int size_tcp = TH_OFF(tcp) * 4;
	const char *payload = (u_char *)(packet + SIZE_ETHERNET + size_ip + size_tcp);
	int size_payload = ntohs(ip->ip_len) - (size_ip + size_tcp);
	
	if(size_payload == 0) return; // skip empty case

	printf("\n%d ", packet_number++); // print Number
	printf("%s:%d ", inet_ntoa(ip->ip_src), ntohs(tcp->th_sport));
	printf("%s:%d ", inet_ntoa(ip->ip_dst), ntohs(tcp->th_dport));
	printf("HTTP ");
	if(ntohs(tcp->th_dport) == 80)
		printf("Request\n");
	else
		printf("Response\n");

	int payload_counter = 0;	// count current bytes
	const u_char *iter = payload;	// pointer of payload
	while(size_payload >= payload_counter){
		if(*iter == 0x0D){ // CR
			iter++;
			payload_counter++;
			if(*iter == 0x0A){ // LF
				iter++;
				payload_counter++;
				if(size_payload >= payload_counter){
					if(*iter == 0x0D){
						iter++;
						payload_counter++;
						if(*iter == 0x0A){
							printf("\n");
							break;
						}
					}
				}
				printf("\n");
				continue;
			}
		}
		if(isprint(*iter))
			printf("%c", *iter);
		iter++;
		payload_counter++;
	}
}

void print_DNS_header(u_char *args, const struct pcap_pkthdr *header, const u_char *packet){
	/* output form */
	// Number S_IP:S_PORT D_IP:D_PORT DNS ID : [0x format]
	// [QR | Opcode | AA | TC | RD | RA | Z | RCODE] <-- binary format
	// QDCOUNT : [0x format]
	// ANCOUNT : [0x format]
	// NSCOUNT : [0x format]
	// ARCOUNT : [0x format]

	// calculate ethernet, ip, tcp, payload start pointer
	const struct sniff_ip *ip = (struct sniff_ip*)(packet + SIZE_ETHERNET);
	int size_ip = IP_HL(ip) * 4;
	const char *payload;
	int size_payload;
	const u_char *iter;
	// divide for tcp/udp case
	if(ip->ip_p == IPPROTO_TCP){
		const struct sniff_tcp *tcp = (struct sniff_tcp*)(packet + SIZE_ETHERNET + size_ip);
		int size_tcp = TH_OFF(tcp) * 4;
		payload = (u_char *)(packet + SIZE_ETHERNET + size_ip + size_tcp);
		size_payload = ntohs(ip->ip_len) - (size_ip + size_tcp);

		if(size_payload == 0) return; // skip empty case
	
		iter = payload;	// pointer of payload
	
		printf("\n%d ", packet_number++); // print Number
		printf("%s:%d ", inet_ntoa(ip->ip_src), ntohs(tcp->th_sport));
		printf("%s:%d ", inet_ntoa(ip->ip_dst), ntohs(tcp->th_dport));
	}
	else if(ip->ip_p == IPPROTO_UDP){
		const struct sniff_udp *udp = (struct sniff_udp*)(packet + SIZE_ETHERNET + size_ip);
		int size_udp = 8; // fixed size : 8byte
		payload = (u_char *)(packet + SIZE_ETHERNET + size_ip + size_udp);
		size_payload = ntohs(ip->ip_len) - (size_ip + size_udp);

		if(size_payload == 0) return; // skip empty case
		
		iter = payload;	// pointer of payload

		printf("\n%d ", packet_number++); // print Number
		printf("%s:%d ", inet_ntoa(ip->ip_src), ntohs(udp->uh_sport));
		printf("%s:%d ", inet_ntoa(ip->ip_dst), ntohs(udp->uh_dport));
	}
	printf("DNS ID : ");
	printf("%02x", *iter);
	iter++;
	printf("%02x\n", *iter);
	iter++;
	ctob(iter); // print 16bit binary format flags
	iter = iter + 2;
	
	printf("QDCOUNT : %02x%02x\n", *iter, *(iter+1));
	iter = iter + 2;
	printf("ANCOUNT : %02x%02x\n", *iter, *(iter+1));
	iter = iter + 2;
	printf("NSCOUNT : %02x%02x\n", *iter, *(iter+1));
	iter = iter + 2;
	printf("ARCOUNT : %02x%02x\n", *iter, *(iter+1));
}

void ctob(const u_char *character){
	// convert char -> dec -> binary
	int decimal1 = (int)(*character);
	int binary1[8];
	for(int i=0; i<7; i++){
		binary1[7-i] = decimal1 % 2;
		decimal1 /= 2;
	}
	binary1[0] = decimal1;
	
	int decimal2 = (int)(*(character+1));
	int binary2[8];
	for(int i=0; i<7; i++){
		binary2[7-i] = decimal2 % 2;
		decimal2 /= 2;
	}
	binary2[0] = decimal2;
	
	// print 16bits
	printf("%d | ", binary1[0]);
	printf("%d%d%d%d | ", binary1[1], binary1[2], binary1[3], binary1[4]);
	printf("%d | ", binary1[5]);
	printf("%d | ", binary1[6]);
	printf("%d | ", binary1[7]);
	printf("%d | ", binary2[0]);
	printf("%d%d%d | ", binary2[1], binary2[2], binary2[3]);
	printf("%d%d%d%d\n", binary2[4], binary2[5], binary2[6], binary2[7]);
}
