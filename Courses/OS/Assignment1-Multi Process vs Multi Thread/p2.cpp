#include <iostream>
#include <chrono>
#include <fstream>
#include <fcntl.h>
#include <string>
#include <wait.h>
#include <unistd.h>

int main(int argc, char* argv[]) {
	// read process number
	int process_number = atoi(argv[1]);
	double total_time_ms = 0;
	pid_t pid, wpid;
	int status = 0;

	/* processing standard input */
	int number, row, column;
	std::cin >> number >> row >> column;

	/* calculate data for dividing */
	int total_filter_number = number;
	int average_filter_number = number / process_number;
	int filter_count = 0; // start filter index
	total_filter_number -= average_filter_number * process_number;

	int processfilterarr[process_number]; // save each process filter number
	std::string iname[process_number]; // save input file name
	std::string oname[process_number]; // save output file name
	for (int i = 0; i < process_number; i++) { 
		iname[i] = std::to_string(i) + "i.txt";
		oname[i] = std::to_string(i) + "o.txt";
		if (total_filter_number > 0) {
			total_filter_number--;
			processfilterarr[i] = average_filter_number + 1;
		}
		else
			processfilterarr[i] = average_filter_number;
	}
	int filter[number][3][row][column];

	// get filter input
	for (int n = 0; n < number; n++) {
		for (int ch = 0; ch < 3; ch++) {
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < column; c++) {
					std::cin >> filter[n][ch][r][c];
				}
			}
		}
	}
	int R, C;
	std::cin >> R >> C;
	int data[3][R + 2][C + 2];
	// get data input and set padding
	for (int ch = 0; ch < 3; ch++) {
		for (int r = 0; r < R + 2; r++) {
			for (int c = 0; c < C + 2; c++) {
				if ((r == 0) || (r == R + 1))
					data[ch][r][c] = 0;
				else {
					if ((c == 0) || (c == C + 1))
						data[ch][r][c] = 0;
					else
						std::cin >> data[ch][r][c];
				}
			}
		}
	}

	/* processing divided input */
	auto start = std::chrono::system_clock::now();
	for (int i = 0; i < process_number; i++) {
		/* make input file */
		std::ofstream fout;
		fout.open(iname[i]);
		// write filter number, row, column			
		fout << processfilterarr[i] << " ";
		fout << row << " " << column << "\n";
		// write filter data
		for (int n = filter_count; n < filter_count + processfilterarr[i]; n++) {
			for (int ch = 0; ch < 3; ch++) {
				for (int r = 0; r < row; r++) {
					for (int c = 0; c < column; c++) {
						fout << filter[n][ch][r][c] << " ";
					}
					fout << "\n";
				}
			}
		}
		// write input data
		fout << R << " " << C << "\n";
		for (int ch = 0; ch < 3; ch++) {
			for (int r = 1; r < R+1; r++) {
				for (int c = 1; c < C+1; c++) {
					fout << data[ch][r][c] << " ";
				}
				fout << "\n";
			}
		}
		fout.close();
		// child process		
		pid = fork();
		if (pid == 0) {
			int in, out;
			char* args[] = { (char*)"./program1", NULL };
			const char* input = iname[i].c_str();
			const char* output = oname[i].c_str();
			// open input and output files
			in = open(input, O_RDONLY);
			out = open(output, O_WRONLY | O_TRUNC | O_CREAT, S_IRUSR | S_IRGRP | S_IWGRP | S_IWUSR);
			// replace standard input with input file
			dup2(in, 0);
			// replace standard output with output file
			dup2(out, 1);
			// close unused file descriptors
			close(in);
			close(out);
			// execute program 1
			execvp("./program1", args);
			exit(0);
		}						
		// pointing next filter
		filter_count += processfilterarr[i];
	}
	// wait for child
	while((wpid = wait(&status))>0);

	auto end = std::chrono::system_clock::now();
	std::chrono::milliseconds delta = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
	total_time_ms += delta.count();

	// read child outputfile and print output
	int f_count = 0;
	std::string new_time[process_number]; // save child process running time
	for (int p = 0; p < process_number; p++) {
		std::ifstream fin;
		std::string name = oname[p];
		fin.open(name);

		int read;
		for (int n = filter_count; n < filter_count + processfilterarr[p]; n++) {
			for (int r = 0; r < R - row + 3; r++) {
				for (int c = 0; c < C - column + 3; c++) {
					fin >> read;
					std::cout << read << " ";					
				}std::cout << "\n";
			}std::cout << "\n";
		}
		std::string time;
		fin >> time;
		new_time[p] = time;
		fin.close();
		f_count += processfilterarr[p];
	}
	// delete temporary file
	for(int p=0; p<process_number; p++){
		remove(oname[p].c_str());
		remove(iname[p].c_str());
	}	
	// print time
	for(int p=0; p<process_number; p++) std::cout << new_time[p] << " ";
	std::cout << std::endl;
	std::cout << "\n" << total_time_ms << std::endl;
}
