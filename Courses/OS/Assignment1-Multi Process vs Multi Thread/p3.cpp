#include <iostream>
#include <pthread.h>
#include <chrono>

void* thread_function(void* arg);

class Shared_data {
	// shared data for passing to thread
public:
	int thread_num;
	int start_index;
	int filter_num;
	int output_row_size;
	int output_column_size;
	int filter_row_size;
	int filter_column_size;
	int data_row_size;
	int data_column_size;
	double thread_time = 0;
	// reference of list
	int* shared_output;
	int* shared_filter;
	int* shared_data;
};

int main(int argc, char* argv[]) {
	// thread setting
	int thread_number = atoi(argv[1]);
	pthread_t thread_handle[thread_number];
	void* thread_result;
	int res;

	double total_tims_ms = 0;
	/* processing standard input */
	int number, row, column;
	std::cin >> number >> row >> column;

	/* calculate data for dividing */
	int total_filter_number = number;
	int average_filter_number = number / thread_number;
	int filter_count = 0; // start filter index
	total_filter_number -= average_filter_number * thread_number;

	int threadfilterarr[thread_number];
	// save each thread filter number
	for (int i = 0; i < thread_number; i++) {
		if (total_filter_number > 0) {
			total_filter_number--;
			threadfilterarr[i] = average_filter_number + 1;
		}
		else
			threadfilterarr[i] = average_filter_number;
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
	
	int output[number][R - row + 3][C - column + 3]; // save output

	/* make passing shared data */
	Shared_data* share[thread_number];
	for (int t = 0; t < thread_number; t++) {
		share[t] = new Shared_data();
		(share[t])->thread_num = t;
		(share[t])->start_index = filter_count;
		(share[t])->filter_num = threadfilterarr[t];
		(share[t])->output_row_size = R-row+3;
		(share[t])->output_column_size = C-column+3;
		(share[t])->filter_row_size = row;
		(share[t])->filter_column_size = column;
		(share[t])->data_column_size = C+2;
		(share[t])->data_row_size = R+2;
		(share[t])->shared_output = &(output[0][0][0]);
		(share[t])->shared_filter = &(filter[0][0][0][0]);
		(share[t])->shared_data = &(data[0][0][0]);
		filter_count += threadfilterarr[t];
	}
	/* make thread */
	auto start = std::chrono::system_clock::now();
	// run thread
	for (int t = 0; t < thread_number; t++) {
		res = pthread_create(&(thread_handle[t]), NULL, thread_function, (void*)(share[t]));		
	}
	// wait thread
	for (int t = 0; t < thread_number; t++) {
		res = pthread_join(thread_handle[t], &thread_result);
	}
	/* caculate time */	
	auto end = std::chrono::system_clock::now();
	std::chrono::milliseconds delta = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
	double total_time_ms = delta.count();

	/* print output */
	for (int n = 0; n < number; n++) {
		for (int r = 0; r < R - row + 3; r++) {
			for (int c = 0; c < C - column + 3; c++) {
				std::cout << output[n][r][c] << " ";
			}
			std::cout << "\n";
		}
		std::cout << "\n";
	}
	for (int t = 0; t < thread_number; t++) std::cout << share[t]->thread_time << " ";
	std::cout << "\n" << total_time_ms << std::endl;
}

void* thread_function(void* arg) {
	// get data
	Shared_data* data = (Shared_data*)arg;
	// set magnitude
	int m_data_row = (data->data_column_size);
	int m_data_total = m_data_row * (data->data_row_size);
	int m_filter_row = (data->filter_column_size);
	int m_filter_total = m_filter_row * (data->filter_row_size);
	int m_output_row = (data->output_column_size);
	int m_output_total = m_output_row * (data->output_row_size);
	/* respresentation for index to pointer */
//	(*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c)))
//	(*((data->shared_filter) + (n * m_filter_total * 3) + (0 * m_filter_total) + (y * m_filter_row) + (x)))
//	(*((data->shared_data) + (0 * m_data_total) + ((r+y) * m_data_row) + (c+x)))

	auto start = std::chrono::system_clock::now();
	// CNN
	for (int n = data->start_index; n < (data->start_index) + (data->filter_num); n++) {
		for (int r = 0; r < data->output_row_size; r++) {
			for (int c = 0; c < data->output_column_size; c++) {
				// output[n][r][c] = 0;
				*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c)) = 0; // initialize
				for (int y = 0; y < data->filter_row_size; y++) {
					for (int x = 0; x < data->filter_column_size; x++) {
//						output[n][r][c] += filter[n][0][y][x] * data[0][r + y][c + x];
//						output[n][r][c] += filter[n][1][y][x] * data[1][r + y][c + x];
//						output[n][r][c] += filter[n][2][y][x] * data[2][r + y][c + x];
						(*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c))) +=
							(*((data->shared_filter) + (n * m_filter_total * 3) + (0 * m_filter_total) + (y * m_filter_row) + (x))) * (*((data->shared_data) + (0 * m_data_total) + ((r + y) * m_data_row) + ((c + x))));
						(*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c))) +=
							(*((data->shared_filter) + (n * m_filter_total * 3) + (1 * m_filter_total) + (y * m_filter_row) + (x))) * (*((data->shared_data) + (1 * m_data_total) + ((r + y) * m_data_row) + ((c + x))));
						(*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c))) +=
							(*((data->shared_filter) + (n * m_filter_total * 3) + (2 * m_filter_total) + (y * m_filter_row) + (x))) * (*((data->shared_data) + (2 * m_data_total) + ((r + y) * m_data_row) + ((c + x))));
					}
				}
			}
		}
	}
	// RELU
	for (int n = data->start_index; n < (data->start_index) + (data->filter_num); n++) {
		for (int r = 0; r < data->output_row_size; r++) {
			for (int c = 0; c < data->output_column_size + 3; c++) {
				if ((*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c))) < 0)
					(*((data->shared_output) + (n * m_output_total) + (r * m_output_row) + (c))) = 0;
			}
		}
	}
	auto end = std::chrono::system_clock::now();
	std::chrono::milliseconds delta = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
	double total_time_ms = delta.count();

	// passing time data to parent
	data->thread_time = total_time_ms;
	
	pthread_exit(NULL);
}
