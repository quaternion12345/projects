#include <stdlib.h>
// #include <cstdio>
#include <iostream>
#include <chrono>
#include <thread>
#include <mutex>
#include <vector>
#include <cmath>

void mac(int tid, int num_threads, const float* k, int N, float* array_in, float* array_out_parallel){
	int start = tid * ceil( (N-2) / num_threads);
	if(N-2 < num_threads){ // case NT > N - 2
		if(tid < N-2){
			for(int j=0; j<3; j++){
				(*(array_out_parallel + tid)) += (*(array_in + tid + j)) * (*(k+j));
			}	
		}
		return;
	}
	if(tid == num_threads-1){ // case last thread
		for(int i=start; i<N-2; i++){
			(*(array_out_parallel +i)) = (*(array_in + i)) * (*k)
						    +(*(array_in + i + 1)) * (*(k+1))
					    	    +(*(array_in + i + 2)) * (*(k+2));
		}
		return;
	}
	for(int i=0; i<(N-2)/num_threads; i++){ // normal case
		(*(array_out_parallel + start +i)) = (*(array_in + start + i)) * (*k)
						    +(*(array_in + start + i + 1)) * (*(k+1))
					   	    +(*(array_in + start + i + 2)) * (*(k+2));
	}
	return;
}

int main(int argc, char** argv) 
{

  if(argc < 2) std::cout<<"Usage : ./filter num_items"<<std::endl;
  int N = atoi(argv[1]);
  int NT=32; //Default value. change it as you like.
  //0. Initialize

  const int FILTER_SIZE=3;
  const float k[FILTER_SIZE] = {0.25, 0.5, 0.25};
  float *array_in = new float[N];
  float *array_out_serial = new float[N];
  float *array_out_parallel = new float[N];
  {
    std::chrono::duration<double> diff;
    auto start = std::chrono::steady_clock::now();
    for(int i=0;i<N;i++) {
      array_in[i] = i;
    }
    auto end = std::chrono::steady_clock::now();
    diff = end-start;
    std::cout<<"init took "<<diff.count()<<" sec"<<std::endl;
  }

  {
    //1. Serial
    std::chrono::duration<double> diff;
    auto start = std::chrono::steady_clock::now();
    for(int i=0;i<N-2;i++) {
      for(int j=0;j<FILTER_SIZE;j++) {
        array_out_serial[i] += array_in[i+j] * k[j];
      }
    }
    auto end = std::chrono::steady_clock::now();
    diff = end-start;
    std::cout<<"serial 1D filter took "<<diff.count()<<" sec"<<std::endl;
  }

  {
    //2. parallel 1D filter
    std::chrono::duration<double> diff;
    auto start = std::chrono::steady_clock::now();
    /* TODO: put your own parallelized 1D filter here */
    /****************/
    std::vector<std::thread> threads; // define threads
    for(int t=0; t<NT; t++){
    	threads.push_back(std::thread(mac, t, NT, k, N, array_in, array_out_parallel));
    }
    for(auto& thread: threads){
	    thread.join();
    }

    /****************/
    /* TODO: put your own parallelized 1D filter here */
    auto end = std::chrono::steady_clock::now();
    diff = end-start;
    std::cout<<"parallel 1D filter took "<<diff.count()<<" sec"<<std::endl;



    int error_counts=0;
    const float epsilon = 0.01;
    for(int i=0;i<N;i++) {
      float err= std::abs(array_out_serial[i] - array_out_parallel[i]);
      if(err > epsilon) {
        error_counts++;
        if(error_counts < 5) {
          std::cout<<"ERROR at "<<i<<": Serial["<<i<<"] = "<<array_out_serial[i]<<" Parallel["<<i<<"] = "<<array_out_parallel[i]<<std::endl;
          std::cout<<"err: "<<err<<std::endl;
        }
      }
    }


    if(error_counts==0) {
      std::cout<<"PASS"<<std::endl;
    } else {
      std::cout<<"There are "<<error_counts<<" errors"<<std::endl;
    }

  }
  return 0;
}
