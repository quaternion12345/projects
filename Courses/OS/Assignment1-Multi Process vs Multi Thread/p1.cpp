#include <iostream>
#include <chrono>
int main(void) {

	int number, row, column;
	std::cin >> number >> row >> column;

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
	int data[3][R+2][C+2];

	// get data input and set padding
	for (int ch = 0; ch < 3; ch++) {			
		for (int r = 0; r < R+2; r++) {	
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
	
	// update data by CNN
	auto start = std::chrono::system_clock::now();

	// CNN
	// CNN's each row and column size is data size - filter size + 1
	int output[number][R - row + 3][C - column + 3];
	for (int n = 0; n < number; n++) {
		for (int r = 0; r < R - row + 3; r++) {
			for (int c = 0; c < C - column + 3; c++) {
				output[n][r][c] = 0;
				for (int y = 0; y < row; y++) {
					for (int x = 0; x < column; x++) {
						output[n][r][c] += filter[n][0][y][x] * data[0][r+y][c+x];
						output[n][r][c] += filter[n][1][y][x] * data[1][r+y][c+x];
						output[n][r][c] += filter[n][2][y][x] * data[2][r+y][c+x];
					}
				}
			}
		}
	}
	// RELU
	for (int n = 0; n < number; n++) {
		for (int r = 0; r < R - row + 3; r++) {
			for (int c = 0; c < C - column + 3; c++) {
				if (output[n][r][c] < 0)
					output[n][r][c] = 0;
			}
		}
	}

	auto end = std::chrono::system_clock::now();
	std::chrono::milliseconds delta = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);

	// print output
	double total_time_ms = delta.count();
	
	for (int n = 0; n < number; n++) {
		for (int r = 0; r < R - row + 3; r++) {
			for (int c = 0; c < C - column + 3; c++) {
				std::cout << output[n][r][c] << " ";
			}
			std::cout << "\n";
		}
		std::cout << "\n";
	}
	std::cout << total_time_ms << std::endl;

	return 0;
}
