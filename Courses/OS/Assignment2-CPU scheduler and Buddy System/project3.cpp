#include <iostream>
#include <fstream>
#include <string>
#include <queue>
#include <stack>

using namespace std;

class Page {
public:
	Page() {
		AllocationID = 0;
		FrameIndex = -1;
		ValidBit = -1;
		referencebit = "00000000";
		r = "0";
		count = 0;
	}
	int AllocationID ;	// Allocation ID
	int FrameIndex;		// Frame Index
	int ValidBit;		// Valid Bit
	string referencebit; // reference bit
	string r; // recent reference bit
	int count; // for counting algorithm
};
class Process {
public:
	Process& operator=(const Process& rhs) {
		pid = rhs.pid;
		createTime = rhs.createTime;
		code = rhs.code;
		sourceOpened = rhs.sourceOpened;
		source = rhs.source;
		current_line = rhs.current_line;
		total_line = rhs.total_line;
		process_started = rhs.process_started;
		process_end = rhs.process_end;
		wakeupTime = rhs.wakeupTime;
		PageTable = rhs.PageTable;
		isbusywait = rhs.isbusywait;
		waitsource = rhs.waitsource;
		comeback = rhs.comeback;
		b_op = rhs.b_op;
		b_arg = rhs.b_arg;
		timequantum = rhs.timequantum;
		cpuburst = rhs.cpuburst;
		return *this;
	}
	int pid;		// ProcessID
	int createTime;	// Created Cycle
	string code;	// code name
	bool sourceOpened = false; // source is already opened
	FILE* source;	// program source
	int current_line;	// current executed line
	int total_line;		// total code line;
	bool process_started = false; // process started
	bool process_end = false;	// process finished
	int wakeupTime;	// wakeup time for sleep or wait
	Page* PageTable;	// Page Table Base Pointer
	bool isbusywait = false;	// process is busy wait now
	int waitsource;	// source for wait
	bool comeback = false;	// comeback from busywait
	int b_op;	// busy wait process saved line
	int b_arg;	// busy wait process saved line
	int timequantum = 0; // time quantum for RR
	double cpuburst; // cpu burst time S[n]
	int Tn;	// T[n]
	int SumTn;	// Sum of T[n]
	int burst_count = 0; // n
};
class IO {
public:
	int createTime;	// Requested Cycle
	int targetPID;	// Target ProcessID
};
class Resource {
public:
	int ID;	// name
	int pid;// resource hold process id
};
class Global {
public:
	string sched = "fcfs";
	string page = "fifo";
	string dir = ".";
	int totalEventNum = 0;
	int vmemSize = 0;
	int pmemSize = 0;
	int pageSize = 0;
	int pagefault = 0;
	int* physicalMemory;
	int* blocksize;
	Process* processTable;
	int processnumber; // total process number for indexing
	int allocationID = 0;  // number of allocation executed;
	queue<Resource> usedResource;	// queue for locked resource
	int resourcenum = 0;  // total locked resource number
	queue<Process> busywait;	// queue for busy wait process
	int bwpnum = 0;	// number of busywait processes
	queue<int> victim_AID; // queue for victim page
	stack<int> victim_AID2; // stack for victim page
	int time_interval = 0; // time interval
	queue<Page> PageOnP;	// Page allocated on physical memory
	int access_counter = 0; // counter for current acess
	int total_acess = 0; // count for total acess occured
	int* acess_ID;	// table base pointer for contains allocation ID
	Process* SprocessTable;	// data for simulation in OPTIMAL ALGORITHM
	int SallocationID = 0;	// data for simulation in OPTIMAL ALGORITHM
	bool compareburst = false; // need to compare burst
	int sjf_breakcycle = 0;	// next adjacent compare cycle
};

Global Data;	// class for sharing datas globally

// function proto type
void simulator();
void Skeleton(FILE* input);
void Scheduler(queue<Process> process, queue<IO> io);
int FCFS(queue<int>* rq);
int RR(queue<int>* rq);
int SJF(queue<int>* rq);
int Code(int& rp, queue<int>* rq, queue<int>* sl, queue<int>* wl, int& cycle, FILE* output_file, FILE* output_file2);
void MemoryAllocation(Process* rp, int num);
void MemoryAccess(Process* rp, int aid, int LastIndex, int size);
void MemoryRelease(Process* rp, int aid, int LastIndex, int num);
void Sleep(queue<int>* sl, Process* rp, int wakeuptime);
void IOwait(queue<int>* wl, Process* rp);
void Lock(Process* rp, int id);
void Unlock(int id);
void BuddySystem();
int BuddySystem(int size);
void FIFO(Process* rp);
void LRU(Process* rp);
void LRUSAMPLED(Process* rp);
void LFU(Process* rp);
void MFU(Process* rp);
void OPTIMAL(Process* rp);
void Simulation(queue<Process> process, queue<IO> io);
int SCode(int& rp, queue<int>* rq, queue<int>* sl, queue<int>* wl, int& cycle);

int main(int argc, char* argv[]) {
	// set user options
	for (int i = 1; i < argc; i++) {
		if (argv[i][1] == 's') {			
			string schedule = argv[i];
			Data.sched = schedule.substr(7);
			continue;
		}
		if (argv[i][1] == 'p') {			
			string page = argv[i];
			Data.page = page.substr(6);
			continue;
		}
		if (argv[i][1] == 'd') {			
			string directory = argv[i];
			Data.dir = directory.substr(5);
			continue;
		}
	}
	simulator();
}

void simulator() {
	// read input file
	string input = Data.dir + "/input";
	FILE* input_file = fopen(input.c_str(), "r");	
	// read first line
	fscanf(input_file, "%d\t%d\t%d\t%d\n", &(Data.totalEventNum), &(Data.vmemSize), &(Data.pmemSize), &(Data.pageSize));
	// read other lines
	Skeleton(input_file);	
}
void Skeleton(FILE* input) {
	// make initial form
	queue<Process> ProcessQueue;	// Process Queue
	queue<IO> IOQueue;				// IO Queue
	int PhysicalMemory[Data.pmemSize / Data.pageSize];	// Physical Memory
	int Block[Data.pmemSize / Data.pageSize]; // block size
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) {
		PhysicalMemory[i] = 0;
		Block[i] = (Data.pmemSize / Data.pageSize);
	}
	Data.physicalMemory = PhysicalMemory;
	Data.blocksize = Block;
	int count = 0;
	// make Process Queue and IO Queue
	for (int i = 0; i < Data.totalEventNum; i++) {		
		int time;
		char CODE[100];
		string code;
		fscanf(input, "%d\t%s", &time, &CODE);
		code = CODE;
		if (code == "INPUT") {
			// I/O operation code
			int target;
			fscanf(input, "%d\n", &target);
			IO temp;
			temp.createTime = time;
			temp.targetPID = target;
			IOQueue.push(temp);
		}
		else {
			// execution code
			// Make Process and its Page Table
			Process temp;
			temp.pid = count++;
			temp.createTime = time;
			temp.code = code;
			Page* PT = new Page[Data.vmemSize / Data.pageSize];	
			temp.PageTable = PT;
			ProcessQueue.push(temp);
		}
	}
	// Make Process Table for control process datas
	Process processtable[ProcessQueue.size()];
	Process Sprocesstable[ProcessQueue.size()];
	Data.processTable = processtable;
	Data.processnumber = ProcessQueue.size();
	Data.SprocessTable = Sprocesstable;
	for (int i = 0; i < ProcessQueue.size(); i++) {
		Process temp = ProcessQueue.front();
		processtable[i] = temp;
		ProcessQueue.pop();
		ProcessQueue.push(temp);
	}
//	Simulation(ProcessQueue, IOQueue);
	Scheduler(ProcessQueue, IOQueue);
}
void Scheduler(queue<Process> process, queue<IO> io) {
	int RP = -1;	//Running Process pid
	queue<int> RQ;	// Run Queue
	queue<int> SL;	// Sleep List
	queue<int> WL;	// I/O Wait List
	int cycle = 1;
	int flag = 0;
	FILE* output_file = fopen("scheduler.txt", "w");
	FILE* output_file2 = fopen("memory.txt", "w");
	// table for write access process ID for using optimal page replacement
	int access_table[10000];
	for (int i = 0; i < 10000; i++) {
		access_table[i] = 0;
	}
	Data.acess_ID = access_table;
	while (!flag) {
		// Sleep process wakes up and move to RunQueue
		if (!(SL.empty())) {
			int len = SL.size();
			for (int sleep = 0; sleep < len; sleep++) {
				// waking up process
				int sp = SL.front();
				SL.pop();
				if ((Data.processTable + sp)->wakeupTime == cycle) {
					RQ.push(sp);
					// renew cpu burst
					if (Data.sched.substr(0, 2) == "sj") {
						Data.compareburst = true;						
						if (Data.sched.substr(0, 5) == "sjf-s") {
							if ((Data.processTable + sp)->burst_count == 1)
								(Data.processTable + sp)->cpuburst = (Data.processTable + sp)->SumTn;
							else
								(Data.processTable + sp)->cpuburst = ((Data.processTable + sp)->SumTn) / (Data.processTable + sp)->burst_count;
						}
						else if (Data.sched.substr(0, 5) == "sjf-e") {
							if ((Data.processTable + sp)->burst_count == 1)
								(Data.processTable + sp)->cpuburst = (Data.processTable + sp)->Tn;
							else
								(Data.processTable + sp)->cpuburst = 0.6 * ((Data.processTable + sp)->Tn) + 0.4 * ((Data.processTable + sp)->cpuburst);
						}
						(Data.processTable + sp)->Tn = 0;
						(Data.processTable + sp)->burst_count++;
					}
				}
				else
					SL.push(sp);
			}
		}
		// IO wait process wakes up and move to RunQueue
		if (!(WL.empty())) {
			IO i;
			int len1 = io.size();
			for (int n = 0; n < len1; n++) {
				i = io.front();
				io.pop();
				if (i.createTime == cycle) {
					int wp;
					int len2 = WL.size();
					for (int wait = 0; wait < len2; wait++) {
						// waking up process
						wp = WL.front();
						WL.pop();
						if (wp == i.targetPID) {
							RQ.push(wp);
							if (Data.sched.substr(0, 2) == "sj") {
								Data.compareburst = true;									
								if (Data.sched.substr(0, 5) == "sjf-s") {
									if ((Data.processTable + wp)->burst_count == 1)
										(Data.processTable + wp)->cpuburst = (Data.processTable + wp)->SumTn;
									else
										(Data.processTable + wp)->cpuburst = ((Data.processTable + wp)->SumTn) / (Data.processTable + wp)->burst_count;
								}
								else if (Data.sched.substr(0, 5) == "sjf-e") {
									if ((Data.processTable + wp)->burst_count == 1)
										(Data.processTable + wp)->cpuburst = (Data.processTable + wp)->Tn;
									else
										(Data.processTable + wp)->cpuburst = 0.6 * ((Data.processTable + wp)->Tn) + 0.4 * ((Data.processTable + wp)->cpuburst);
								}
								(Data.processTable + wp)->Tn = 0;
								(Data.processTable + wp)->burst_count++;
							}
						}
						else {
							WL.push(wp);
						}
					}
				}
				else {
					io.push(i);
				}
			}
			// write data for next compare cycle
			if (!(io.empty())) {
				if (Data.sjf_breakcycle == 0)
					Data.sjf_breakcycle = io.front().createTime;
				else
					Data.sjf_breakcycle = min(Data.sjf_breakcycle, io.front().createTime);
			}
		}
		// Process generate
		if (!(process.empty())) {
			Process p = process.front();
			if (p.createTime == cycle) {
				// time to schedule, then schedule process
				process.pop();
				(Data.processTable + p.pid)->process_started = true;
				(Data.processTable + p.pid)->cpuburst = 5;
				(Data.processTable + p.pid)->Tn = 0;
				(Data.processTable + p.pid)->SumTn = 0;
				(Data.processTable + p.pid)->burst_count++;
				Data.compareburst = true;
				RQ.push(p.pid);
			}
			// write data for next compare cycle
			if (!(process.empty())) {
				if (Data.sjf_breakcycle == 0)
					Data.sjf_breakcycle = process.front().createTime;
				else
					Data.sjf_breakcycle = min(Data.sjf_breakcycle, process.front().createTime);
			}
		}
		// Get Process and Execute Process Code and Write Output File
		flag = Code(RP, &RQ, &SL, &WL, cycle, output_file, output_file2);
		// next cycle
		cycle++;
	}
	fprintf(output_file2, "page fault = %d\n", Data.pagefault);
	fclose(output_file);
	fclose(output_file2);
}
int FCFS(queue<int>* rq) {
	int p = rq->front();
	rq->pop();
	return p;
}

int RR(queue<int>* rq) {
	int p = rq->front();
	rq->pop();
	return p;
}

int SJF(queue<int>* rq) {
	int len = rq->size();
	int RunP = rq->front();
	double burst_time = (Data.processTable + RunP)->cpuburst;
	// find minimum cpu burst process
	for (int i = 0; i < len; i++) {
		int temp = rq->front();
		rq->pop();
		rq->push(temp);
		if ((Data.processTable + temp)->cpuburst < burst_time) {
			burst_time = (Data.processTable + RunP)->cpuburst;
			RunP = temp;
		}
	}
	// remove target process from run Queue
	for (int i = 0; i < len; i++) {
		int temp = rq->front();
		rq->pop();
		if (RunP == temp)
			continue;
		else
			rq->push(temp);		
	}
	return RunP;
}

int Code(int& rp, queue<int>* rq, queue<int>* sl, queue<int>* wl, int& cycle, FILE* output_file, FILE* output_file2) {
	bool SPExist = false; // boolean flag for check Scheduled process exists
	if (rp == -1) {
		if (rq->empty()) {
			if (sl->empty() && wl->empty()) // program ends
				return 1;
		}
		else {
			// select running process by using scheduler
			int p;
			if (Data.sched == "fcfs")
				p = FCFS(rq);
			else if (Data.sched == "rr")
				p = RR(rq);
			else if (Data.sched == "sjf-simple")
				p = SJF(rq);
			else if (Data.sched == "sjf-exponential")
				p = SJF(rq);
			rp = p;
			SPExist = true;
		}
	}
	else if (Data.compareburst) {
		// check for preemption about SJF-algorithm
		double rpburst = (Data.processTable+rp)->cpuburst - (Data.processTable+rp)->Tn;
		int RunP = rp;
		int len = rq->size();
		for (int i = 0; i < len; i++) {
			int temp = rq->front();
			rq->pop();
			rq->push(temp);
			if ((Data.processTable + temp)->cpuburst < rpburst) {
				RunP = temp;
				rpburst = (Data.processTable + temp)->cpuburst;
			}
		}
		if (RunP != rp) {
		// Running Process preempted
			(Data.processTable + rp)->cpuburst -= (Data.processTable + rp)->Tn;
			int length = rq->size();
			for (int i = 0; i < length; i++) {
				int tmp = rq->front();
				rq->pop();
				if (tmp == RunP) {
					continue;
				}
				else
					rq->push(tmp);
			}
			rq->push(rp);
			rp = RunP;
		}
		Data.compareburst = false;
	}
	// open running process source
	// get line, op, arg
	int op = -1;
	int arg = -1;
	if (rp != -1) {
		// if not NO-OP
		(Data.processTable + rp)->timequantum++;
		if (!((Data.processTable + rp)->sourceOpened)){ // if process generated then read first line
			string filename = Data.dir + "/" + (Data.processTable + rp)->code;
			(Data.processTable + rp)->source = fopen(filename.c_str(), "r");
			(Data.processTable + rp)->sourceOpened = true;
			fscanf((Data.processTable + rp)->source, "%d\n", &((Data.processTable + rp)->total_line));
			(Data.processTable + rp)->current_line = 0;
		}
		if (((Data.processTable + rp)->comeback) || ((Data.processTable + rp)->isbusywait)) {
			// if busy wait or come back from busy wait
			op = (Data.processTable + rp)->b_op;
			arg = (Data.processTable + rp)->b_arg;
		}
		else {
			// general case for read process code
			Data.time_interval++;
			(Data.processTable + rp)->current_line += 1;
			fscanf((Data.processTable + rp)->source, "%d\t%d\n", &op, &arg);
			if ((Data.processTable + rp)->current_line == (Data.processTable + rp)->total_line)
				(Data.processTable + rp)->process_end = true;
		}
	}
	// do process code file
	int allocid;
	int pagenum;
	string func;
	if (op == 0) {
		allocid = (Data.allocationID + 1);
		pagenum = arg;
		func = "ALLOCATION";
		MemoryAllocation((Data.processTable + rp), arg);
	}
	else if (op == 1) {
		allocid = arg;
		pagenum = 0;
		int last_index;
		for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) {
			if (((Data.processTable + rp)->PageTable + i)->AllocationID == allocid) {
				pagenum++;
				last_index = i;
			}
		}
		func = "ACCESS";
		MemoryAccess((Data.processTable + rp), arg, last_index, pagenum);
	}
	else if (op == 2) {
		allocid = arg;
		pagenum = 0;
		int last_index;
		for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) {
			if (((Data.processTable + rp)->PageTable + i)->AllocationID == allocid) {
				pagenum++;
				last_index = i;
			}
		}
		func = "RELEASE";
		MemoryRelease((Data.processTable + rp), arg, last_index, pagenum);
	}
	else if (op == 3) {
		func = "NONMEMORY";
	}
	else if (op == 4) {
		func = "SLEEP";
		Sleep(sl, (Data.processTable + rp), cycle + arg);
	}
	else if (op == 5) {
		func = "IOWAIT";
		IOwait(wl, (Data.processTable + rp));
	}
	else if (op == 6) {
		Lock((Data.processTable + rp), arg);
		func = "LOCK";
	}
	else if (op == 7) {
		Unlock(arg);
		func = "UNLOCK";
	}
	// open write files
	
	do {
		// write scheduler.txt	
		// Line 1	
		fprintf(output_file, "[%d Cycle] Scheduled Process: ", cycle);
		if (SPExist)
			fprintf(output_file, "%d %s\n", (Data.processTable + rp)->pid, (Data.processTable + rp)->code.c_str());
		else
			fprintf(output_file, "None\n");
		// Line 2
		fprintf(output_file, "Running Process: ");
		if (rp != -1)
			fprintf(output_file, "Process#%d running code %s line %d(op %d, arg %d)\n", (Data.processTable + rp)->pid, (Data.processTable + rp)->code.c_str(), (Data.processTable + rp)->current_line, op, arg);
		else
			fprintf(output_file, "None\n");
		// Line 3
		fprintf(output_file, "RunQueue: ");
		if (rq->empty())
			fprintf(output_file, "Empty");
		else {
			for (int i = 0; i < rq->size(); i++) {
				int temp = rq->front();
				fprintf(output_file, "%d(%s) ", temp, (Data.processTable + temp)->code.c_str());
				rq->pop();
				rq->push(temp);
			}
		}
		fprintf(output_file, "\n");
		// Line 4
		fprintf(output_file, "SleepList: ");
		if (sl->empty())
			fprintf(output_file, "Empty");
		else {
			for (int i = 0; i < sl->size(); i++) {
				int temp = sl->front();
				fprintf(output_file, "%d(%s) ", temp, (Data.processTable + temp)->code.c_str());
				sl->pop();
				sl->push(temp);
			}
		}
		fprintf(output_file, "\n");
		// Line 5
		fprintf(output_file, "IOWait List: ");
		if (wl->empty())
			fprintf(output_file, "Empty\n");
		else {
			for (int i = 0; i < wl->size(); i++) {
				int temp = wl->front();
				fprintf(output_file, "%d(%s) ", temp, (Data.processTable + temp)->code.c_str());
				wl->pop();
				wl->push(temp);
			}
			fprintf(output_file, "\n");
		}
		fprintf(output_file, "\n");

		// write memory.txt	
		// Line 1
		if (rp != -1) {
			if (op < 3)
				fprintf(output_file2, "[%d Cycle] Input : Pid [%d] Function [%s] Alloc ID [%d] Page Num[%d]\n", cycle, (Data.processTable + rp)->pid, func.c_str(), allocid, pagenum);
			else
				fprintf(output_file2, "[%d Cycle] Input : Pid [%d] Function [%s]\n", cycle, (Data.processTable + rp)->pid, func.c_str());
		}
		else
			fprintf(output_file2, "[%d Cycle] Input : Function [NO-OP]\n", cycle);
		// Line 2
		// physical memory
		fprintf(output_file2, "%-30s", ">> Physical Memory : ");
		for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) {
			if (i % 4 == 0)
				fprintf(output_file2, "|");
			if (*(Data.physicalMemory + i) != 0)
				fprintf(output_file2, "%d", *(Data.physicalMemory + i));
			else
				fprintf(output_file2, "-");
		}
		fprintf(output_file2, "|\n");
		// page table
		for (int i = 0; i < Data.processnumber; i++) {			
			Process temp = *(Data.processTable + i);
			if (temp.process_started) {
				// AID
				fprintf(output_file2, ">> pid(%d) %-20s", temp.pid, "Page Table(AID) : ");
				for (int j = 0; j < (Data.vmemSize / Data.pageSize); j++) {
					if (j % 4 == 0)
						fprintf(output_file2, "|");
					if ((temp.PageTable + j)->AllocationID == 0)
						fprintf(output_file2, "-");
					else
						fprintf(output_file2, "%d", (temp.PageTable + j)->AllocationID);
				}
				fprintf(output_file2, "|\n");

				// Valid
				fprintf(output_file2, ">> pid(%d) %-20s", temp.pid, "Page Table(Valid) : ");
				for (int j = 0; j < (Data.vmemSize / Data.pageSize); j++) {
					if (j % 4 == 0)
						fprintf(output_file2, "|");
					if ((temp.PageTable + j)->ValidBit >= 0)
						fprintf(output_file2, "%d", (temp.PageTable + j)->ValidBit);
					else
						fprintf(output_file2, "-");
				}
				fprintf(output_file2, "|\n");
			}
		}
		fprintf(output_file2, "\n");		
		// case for no-op
		if (rp == -1)
			break;
		// check for preempt busy waiting process
		if ((Data.sched == "rr") && ((Data.processTable + rp)->timequantum == 10)) { // rr
			(Data.processTable + rp)->timequantum = 0;
			rq->push(rp);
			rp = -1;
			break;
		}
		if ((Data.sched.substr(0,3) == "sjf")) { // sjf
			(Data.processTable + rp)->Tn += 1;
			(Data.processTable + rp)->SumTn += 1;
			// if cycle for compare burst then break
			if (cycle+1 == Data.sjf_breakcycle)
				break;
			int breakcycle = 0;
			for (int i = 0; i < sl->size(); i++) {
				int temp = sl->front();
				sl->pop();
				sl->push(temp);
				if (breakcycle == 0) {
					breakcycle = (Data.processTable + temp)->wakeupTime;
				}
				else {
					breakcycle = min(breakcycle, (Data.processTable + temp)->wakeupTime);
				}
			}
			if (cycle + 1 == breakcycle)
				break;
		}
		if ((Data.processTable + rp)->isbusywait) { // go busy wait next cycle
			cycle++;
			(Data.processTable + rp)->timequantum++;
		}
	}while ((Data.processTable + rp)->isbusywait);
	// ready for next cycle
	
	// if current cylce is sleep or I/O wait
	if ((op == 4) || (op == 5)) {
		if ((Data.processTable + rp)->process_end) { // last cycle is sleep or I/O Wait
			fclose((Data.processTable + rp)->source);
			(Data.processTable + rp)->PageTable = NULL;
			(Data.processTable + rp)->process_started = false;
		}
		if (rp != -1) { // not last cycle then preempt
			(Data.processTable + rp)->timequantum = 0;
			rp = -1;
		}
	}
	// if running process finished
	if ((rp != -1) && ((Data.processTable + rp)->process_end)) {
		fclose((Data.processTable + rp)->source);
		for (int j = 0; j < (Data.vmemSize / Data.pageSize); j++) {
			if (((Data.processTable + rp)->PageTable + j)->ValidBit == 1) { // release finished process' pages on physical memory
				for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) {
					if (*(Data.physicalMemory + i) == ((Data.processTable + rp)->PageTable + j)->AllocationID) {
						*(Data.physicalMemory + i) = 0;
					}
				}
				int len1 = Data.PageOnP.size();
				for (int k = 0; k < len1; k++) { // renew Page Queue for containing Pages on Physical Memory
					Page pg = Data.PageOnP.front();
					Data.PageOnP.pop();
					if (pg.AllocationID == ((Data.processTable + rp)->PageTable + j)->AllocationID)
						continue;
					else
						Data.PageOnP.push(pg);
				}
				int len2 = Data.victim_AID.size();
				for (int k = 0; k < len2; k++) { // renew FIFO Queue for containing Pages on Physical Memory
					int number = Data.victim_AID.front();
					Data.victim_AID.pop();
					if (number == ((Data.processTable + rp)->PageTable + j)->AllocationID)
						continue;
					else
						Data.victim_AID.push(number);
				}
				int len3 = Data.victim_AID2.size();
				stack<int> temp;
				for (int k = 0; k < len3; k++) { // renew LRU Stack for containing Pages on Physical Memory
					int number = Data.victim_AID2.top();
					Data.victim_AID2.pop();
					if (number == ((Data.processTable + rp)->PageTable + j)->AllocationID)
						continue;
					else
						temp.push(number);
				}
				while (!(temp.empty())) {
					int n = temp.top();
					temp.pop();
					Data.victim_AID2.push(n);
				}
			}			
		}		
		(Data.processTable + rp)->PageTable = NULL;
		(Data.processTable + rp)->process_started = false;		
		rp = -1;		
	}
	// if time interval is 8, then update reference byte
	if (Data.time_interval == 8) {
		int len = Data.PageOnP.size();
		for (int i = 0; i < len; i++) {
			Page tmp = Data.PageOnP.front();
			Data.PageOnP.pop();
			tmp.referencebit = tmp.r + tmp.referencebit.substr(1);
			tmp.r = "0";
			Data.PageOnP.push(tmp);
		}
		Data.time_interval = 0;
	}
	// go to next cycle	
	fflush(output_file);
	fflush(output_file2);
	return 0;
}
void MemoryAllocation(Process* rp, int num) {
	Data.allocationID++;
	int count = 0;
	int index = 0;
	// find fitting place
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) {
		if (((rp->PageTable + i)->ValidBit) == -1)
			count++;
		else
			count = 0;
		if (count == num) {
			index = i;
			break;
		}
	}
	// push on Virtual Memory
	for (int i = index + 1 - num; i < index + 1; i++) {
		((rp->PageTable + i)->ValidBit) = 0;
		((rp->PageTable + i)->AllocationID) = Data.allocationID;
	}
}

void MemoryAccess(Process* rp, int aid, int LastIndex, int size) {
	Data.access_counter++;
	// renew LRU stack
	int len = Data.victim_AID2.size();
	stack<int> temp;
	for (int i = 0; i < len; i++) { // check if access is occured at page already on Physical Memory
		int id = Data.victim_AID2.top();
		Data.victim_AID2.pop();
		if (id == aid)
			continue;
		else {
			temp.push(id);
		}
	}
	len = temp.size();
	for (int i = 0; i < len; i++) {
		int number = temp.top();
		temp.pop();
		Data.victim_AID2.push(number);
	}
	Data.victim_AID2.push(aid);
	// check already on physical memory
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) {		
		if (*(Data.physicalMemory + i) == aid) {
			int len = Data.PageOnP.size();
			for (int k = 0; k < len; k++) {
				Page tmp = Data.PageOnP.front();
				Data.PageOnP.pop();
				if (tmp.AllocationID == aid) {
					tmp.r = "1";
					tmp.count++;
				}
				Data.PageOnP.push(tmp);
			}
			return;
		}
	}	
	// first access
	Data.pagefault++;
	int space = 1;
	while (space < size) {
		space *= 2;
	}
	// buddy system merge part
	BuddySystem();
	int index;
	// buddy system divide part
	index = BuddySystem(space);
	if (index == -1) {
		// do page replacement
		Data.pagefault--;
		while (index == -1) {
			if (Data.page == "fifo")
				FIFO(rp);
			else if (Data.page == "lru")
				LRU(rp);
			else if (Data.page == "lru-sampled")
				LRUSAMPLED(rp);
			else if (Data.page == "lfu")
				LFU(rp);
			else if (Data.page == "mfu")
				MFU(rp);
			else if (Data.page == "optimal")
				OPTIMAL(rp);
			Data.pagefault++;
			BuddySystem();
			index = BuddySystem(space);
		}
	}
	// set aid and frame index
	int count = 0;
	for (int i = index; i < index + space; i++) {
		*(Data.physicalMemory + i) = aid;
		if (count < size) {
			(rp->PageTable + LastIndex - size + 1 + count)->FrameIndex = i;
			(rp->PageTable + LastIndex - size + 1 + count)->ValidBit = 1;
			count++;
		}
	}
	// Data Structure for Page Replacement
	Data.victim_AID.push(aid);
	// push page on queue
	Page p;
	p.AllocationID = aid;
	p.r = "1";
	p.count++;
	Data.PageOnP.push(p);
}

void MemoryRelease(Process* rp, int aid, int LastIndex, int num) {
	int firstIndex = LastIndex - num + 1;
	for (int i = firstIndex; i < LastIndex + 1; i++) { // Release Virtual Memory
		(rp->PageTable + i)->AllocationID = 0;
		(rp->PageTable + i)->FrameIndex = -1;
		(rp->PageTable + i)->ValidBit = -1;
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // Release Physical Memory
		if((*(Data.physicalMemory + i)) == aid)
			*(Data.physicalMemory + i) = 0;
	}
	int len = Data.PageOnP.size();
	for (int i = 0; i < len; i++) { // update Page Queue on released page
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (tmp.AllocationID == aid)
			continue;
		else
			Data.PageOnP.push(tmp);
	}
	len = Data.victim_AID.size();
	for (int i = 0; i < len; i++) { // update FIFO Queue on released page
		int tmp = Data.victim_AID.front();
		Data.victim_AID.pop();
		if (tmp == aid)
			continue;
		else
			Data.victim_AID.push(tmp);
	}
	len = Data.victim_AID2.size(); 
	stack<int> middle;
	for(int i=0; i<len; i++){ // update LRU Stack on released page
		int tmp = Data.victim_AID2.top();
		Data.victim_AID2.pop();
		if (tmp == aid)
			continue;
		else
			middle.push(tmp);
	}
	len = middle.size();
	for (int i = 0; i < len; i++) {
		Data.victim_AID2.push(middle.top());
		middle.pop();
	}
}

void Sleep(queue<int>* sl, Process* rp, int wakeuptime) {	
	// get sleep and set wakeup time
	if (rp->process_end)
		return;
	else {
		int time = wakeuptime;
		rp->wakeupTime = time;
		sl->push(rp->pid);
	}
}

void IOwait(queue<int>* wl, Process* rp) {
	// do wait
	if (rp->process_end)
		return;
	else
		wl->push(rp->pid);
}

void Lock(Process* rp, int id) {	
	if (rp->isbusywait)
		return;
	for (int i = 0; i < Data.resourcenum; i++) {
		Resource r = Data.usedResource.front();
		Data.usedResource.pop();
		Data.usedResource.push(r);
		if (r.ID == id) {
			if (r.pid == rp->pid) { // resource hold process lock again
				return;
			}
			else {	// go busy waiting
				rp->isbusywait = true;
				rp->waitsource = id;
				rp->b_arg = 6;
				rp->b_op = id;
				Data.bwpnum++;
				Data.busywait.push(*rp);
				return;
			}
		}
	}
	// make resource information using Queue
	Resource r;
	r.ID = id;
	r.pid = rp->pid;
	Data.usedResource.push(r);
	Data.resourcenum++;	
}

void Unlock(int id) {
	int target;
	for (int i = 0; i < Data.resourcenum; i++) { // find for unlock resource
		Resource r = Data.usedResource.front();
		Data.usedResource.pop();
		if (r.ID == id) {
			Data.resourcenum--;
			target = id;
			break;
		}
		else {
			Data.usedResource.push(r);
		}
	}
	int len = Data.bwpnum;
	for (int i = 0; i < len; i++) { // release busy wait processes
		Process temp = Data.busywait.front();
		Data.busywait.pop();
		if (target == temp.waitsource) {
			temp.isbusywait = false;
			temp.comeback = true;
			Data.bwpnum--;
		}
		else {
			Data.busywait.push(temp);
		}
	}
}

void BuddySystem() {
	// merge part
	for (int i = 1; i < (Data.pmemSize / Data.pageSize); i *= 2) {
		// merge block
		int stride = 2 * i;
		for (int j = 0; j < (Data.pmemSize / Data.pageSize); j += stride) {
			if ((*(Data.blocksize + j) == i) && (*(Data.physicalMemory + j) == 0) && (*(Data.physicalMemory + j + i) == 0)) {
				if ((*(Data.blocksize + j)) == (*(Data.blocksize + j + i))) {
					for (int k = j; k < j + stride; k++) {
						*(Data.blocksize + k) = stride;
					}
				}
			}
		}
	}
}

int BuddySystem(int size) {
	// divide part
	int index;
	// find fitting size block
	for (int i = 0; i <(Data.pmemSize / Data.pageSize) ; i += size) {
		if ((*(Data.physicalMemory + i) == 0) && (*(Data.blocksize + i) == size)) {
			index = i;
			return index;
		}
	}
	int free_block_size = 0;
	bool escape = false;
	// find for free larger block
	for (int i = size * 2; i<= (Data.pmemSize / Data.pageSize); i *=2) {
		for (int j = 0; j< (Data.pmemSize / Data.pageSize); j += i) {
			if ((*(Data.physicalMemory + j) == 0) && (*(Data.blocksize + j) == i)) {
				index = j;
				free_block_size = i;
				escape = true;
				break;
			}
		}
		if (escape)
			break;
	}
	if(free_block_size == 0)
		return -1;
	else {
		while (free_block_size != size) { // dividing large free block to small fitting block
			int temp = free_block_size / 2;
			for (int i = index; i < index + free_block_size; i++) {
				*(Data.blocksize + i) = temp;
			}
			free_block_size = temp;
		}
		return index;
	}
}

void FIFO(Process* rp) {
	int victim = Data.victim_AID.front();
	Data.victim_AID.pop();
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) { // update victim page Virtual Memory data
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // release victim page on Physical Memory
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
}

void LRU(Process* rp) {
	int len = Data.victim_AID2.size();	
	stack<int> temp;
	for (int i = 0; i < len - 1; i++) {
		int id = Data.victim_AID2.top();
		Data.victim_AID2.pop();
		temp.push(id);
	}
	int victim = Data.victim_AID2.top(); // select victim page
	Data.victim_AID2.pop();
	len = temp.size();
	for (int i = 0; i < len; i++) { // restore
		int id = temp.top();
		temp.pop();
		Data.victim_AID2.push(id);
	}
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) { // update victim page Virtual Memory data
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // release victim page on Physical Memory
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
}

void LRUSAMPLED(Process* rp) {
	int len = Data.PageOnP.size();
	int victim = 0;
	int minimum = 256;
	for (int i = 0; i < len; i++) { // convert reference byte to decimal number 
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		int count = 0;
		int mul = 128;
		for (int j = 0; j < tmp.referencebit.size(); j++) {
			if (tmp.referencebit[j] == '1')
				count += mul;
			mul /= 2;
		}
		if (count < minimum) {
			minimum = count;
			victim = tmp.AllocationID;
		}
		else if (count == minimum) {
			victim = min(victim, tmp.AllocationID);
		}
		Data.PageOnP.push(tmp);
	}
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) { // update victim page Virtual Memory data
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // release victim page on Physical Memory
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
	len = Data.PageOnP.size();
	for (int i = 0; i < len; i++) { // release page from Page Queue for containing page on Physical Memory 
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (tmp.AllocationID == victim)
			continue;
		else
			Data.PageOnP.push(tmp);
	}
}

void LFU(Process* rp) {
	int len = Data.PageOnP.size();
	int victim;
	int minimum;
	for (int i = 0; i < len; i++) { // find least requently used page
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (i == 0) {
			minimum = tmp.count;
			victim = tmp.AllocationID;
		}
		else {
			if (tmp.count < minimum) {
				victim = tmp.AllocationID;
				minimum = tmp.count;
			}
			else if (tmp.count == minimum) {
				victim = min(victim, tmp.AllocationID);
			}
		}
		Data.PageOnP.push(tmp);
	}
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) { // update victim page Virtual Memory data
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // release victim page on Physical Memory
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
	len = Data.PageOnP.size();
	for (int i = 0; i < len; i++) { // deleting victim page on Queue for containing Pages on Physical Memory
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (tmp.AllocationID == victim)
			continue;
		else
			Data.PageOnP.push(tmp);
	}
}

void MFU(Process* rp) {
	int len = Data.PageOnP.size();
	int victim;
	int maximum;
	for (int i = 0; i < len; i++) { // find most frequently used page
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (i == 0) {
			maximum = tmp.count;
			victim = tmp.AllocationID;
		}
		else {
			if (tmp.count > maximum) {
				victim = tmp.AllocationID;
				maximum = tmp.count;
			}
			else if (tmp.count == maximum) {
				victim = min(victim, tmp.AllocationID);
			}
		}
		Data.PageOnP.push(tmp);
	}
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) { // update victim page Virtual Memory data 
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) { // release victim page on Physical Memory
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
	len = Data.PageOnP.size();
	for (int i = 0; i < len; i++) { // deleting victim page on Queue for containing Pages on Physical Memory
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (tmp.AllocationID == victim)
			continue;
		else
			Data.PageOnP.push(tmp);
	}
}

void OPTIMAL(Process* rp) {
	// OPTIMAL ALGORITHM IS NOT COMPLETED
	int victim = 0;
	int access_time = 0;
	// find Victim Page
	for (int i = 0; i < Data.PageOnP.size(); i++) {
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		Data.PageOnP.push(tmp);
		int next_access = -1;
		for (int j = Data.access_counter; j < Data.total_acess; j++) {
			if (*(Data.acess_ID + j) == tmp.AllocationID) {
				next_access = j;
				break;
			}
		}
		if (next_access > access_time) {
			victim = tmp.AllocationID;
			access_time = next_access;
		}
		if (next_access == -1) { // not accessed
			if(victim == 0)
				victim = tmp.AllocationID;
			else {
				victim = min(victim, tmp.AllocationID);
			}
		}		
	}
	// Release Victim Page
	for (int i = 0; i < (Data.vmemSize / Data.pageSize); i++) {
		if ((rp->PageTable + i)->AllocationID == victim) {
			(rp->PageTable + i)->FrameIndex = -1;
			(rp->PageTable + i)->ValidBit = 0;
		}
	}
	for (int i = 0; i < (Data.pmemSize / Data.pageSize); i++) {
		if (*(Data.physicalMemory + i) == victim) {
			*(Data.physicalMemory + i) = 0;
		}
	}
	int len = Data.PageOnP.size();
	for (int i = 0; i < len; i++) {
		Page tmp = Data.PageOnP.front();
		Data.PageOnP.pop();
		if (tmp.AllocationID == victim)
			continue;
		else
			Data.PageOnP.push(tmp);
	}
}

void Simulation(queue<Process> process, queue<IO> io) {
	// function for simulating program
	// SIMULATION IS NOT COMPLETED
	int SRP = -1;	//Running Process pid
	queue<int> SRQ;	// Run Queue
	queue<int> SSL;	// Sleep List
	queue<int> SWL;	// I/O Wait List
	int Scycle = 1;
	int Sflag = 0;
	int access_table[10000];
	for (int i = 0; i < 10000; i++) {
		access_table[i] = 0;
	}
	Data.acess_ID = access_table;
	while (!Sflag) {
		// Sleep process wakes up and move to RunQueue
		if (!(SSL.empty())) {
			int len = SSL.size();
			for (int sleep = 0; sleep < len; sleep++) {
				// waking up process
				int sp = SSL.front();
				SSL.pop();
				if ((Data.SprocessTable + sp)->wakeupTime == Scycle) {
					SRQ.push(sp);
				}
				else
					SSL.push(sp);
			}
		}
		// IO wait process wakes up and move to RunQueue
		if (!(SWL.empty())) {
			IO i;
			int len1 = io.size();
			for (int n = 0; n < len1; n++) {
				i = io.front();
				io.pop();
				if (i.createTime == Scycle) {
					int wp;
					int len2 = SWL.size();
					for (int wait = 0; wait < len2; wait++) {
						// waking up process
						wp = SWL.front();
						SWL.pop();
						if (wp == i.targetPID) {
							SRQ.push(wp);
						}
						else {
							SWL.push(wp);
						}
					}
				}
				else {
					io.push(i);
				}
			}
		}
		// Process generate
		if (!(process.empty())) {
			Process p = process.front();
			if (p.createTime == Scycle) {
				// time to schedule, then schedule process
				process.pop();
				(Data.SprocessTable + p.pid)->process_started = true;
				SRQ.push(p.pid);
			}
		}
		// Get Process and Execute Process Code and Write Output File
		Sflag = SCode(SRP, &SRQ, &SSL, &SWL, Scycle);
		// next cycle
		Scycle++;
	}
}

int SCode(int& rp, queue<int>* rq, queue<int>* sl, queue<int>* wl, int& cycle) {
	// function for simulating program
	// SCODE IS NOT COMPLETED
	bool SPExist = false;
	if (rp == -1) {
		if (rq->empty()) {
			if (sl->empty() && wl->empty()) // program ends
				return 1;
		}
		else {
			// select running process by using scheduler
			int p;
			if (Data.sched == "fcfs")
				p = FCFS(rq);
			else if (Data.sched == "rr")
				p = RR(rq);
			else if (Data.sched == "sjf-simple")
				p = SJF(rq);
			else if (Data.sched == "sjf-exponential")
				p = SJF(rq);
			rp = p;
			SPExist = true;
		}
	}
	// open running process source
	// get line, op, arg
	int op = -1;
	int arg = -1;
	if (rp != -1) {
		(Data.SprocessTable + rp)->timequantum++;
		if (!((Data.SprocessTable + rp)->sourceOpened)) {
			string filename = Data.dir + "/" + (Data.processTable + rp)->code;
			(Data.SprocessTable + rp)->source = fopen(filename.c_str(), "r");
			(Data.SprocessTable + rp)->sourceOpened = true;
			fscanf((Data.SprocessTable + rp)->source, "%d\n", &((Data.SprocessTable + rp)->total_line));
			(Data.SprocessTable + rp)->current_line = 0;
		}
		if ((Data.SprocessTable + rp)->comeback) {
			op = (Data.SprocessTable + rp)->b_op;
			arg = (Data.SprocessTable + rp)->b_arg;
		}
		else {
			Data.time_interval++;
			(Data.SprocessTable + rp)->current_line += 1;
			fscanf((Data.SprocessTable + rp)->source, "%d\t%d\n", &op, &arg);
			if ((Data.SprocessTable + rp)->current_line == (Data.SprocessTable + rp)->total_line)
				(Data.SprocessTable + rp)->process_end = true;
		}
	}
	// do process code file
	int allocid;
	int pagenum;
	string func;	
	if (op == 1) {		
		*(Data.acess_ID + (Data.total_acess++)) = arg;
	}
	else if (op == 4) {
		Sleep(sl, (Data.SprocessTable + rp), cycle + arg);
	}
	else if (op == 5) {
		IOwait(wl, (Data.SprocessTable + rp));
	}
	else if (op == 6) {
		Lock((Data.SprocessTable + rp), arg);		
	}
	else if (op == 7) {
		Unlock(arg);
	}
	do {	
		// case for no-op
		if (rp == -1)
			break;
		// check for preempt busy waiting process
		if ((Data.sched == "rr") && ((Data.SprocessTable + rp)->timequantum == 10)) {
			(Data.SprocessTable + rp)->timequantum = 0;
			rq->push(rp);
			rp = -1;
			break;
		}
		if ((Data.sched == "sjf-simple") || (Data.sched == "sjf-exponential")) {
			break;
		}
		if ((Data.SprocessTable + rp)->isbusywait) {
			cycle++;
			(Data.SprocessTable + rp)->timequantum++;
		}
	} while ((Data.SprocessTable + rp)->isbusywait);
	// ready for next cycle
	// if no-op

	// if current cylce is sleep or I/O wait
	if ((op == 4) || (op == 5)) {
		if ((Data.SprocessTable + rp)->process_end) {
			fclose((Data.SprocessTable + rp)->source);
			(Data.SprocessTable + rp)->PageTable = NULL;
			(Data.SprocessTable + rp)->process_started = false;
		}
		if (rp != -1) {
			(Data.SprocessTable + rp)->timequantum = 0;
			rp = -1;
		}
	}
	// if running process finished
	if ((rp != -1) && ((Data.SprocessTable + rp)->process_end)) {
		fclose((Data.SprocessTable + rp)->source);		
		(Data.SprocessTable + rp)->PageTable = NULL;
		(Data.SprocessTable + rp)->process_started = false;
		rp = -1;
	}
	// go to next cycle	
	return 0;
}