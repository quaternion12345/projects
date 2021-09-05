#include <stdio.h>
#include <iostream>
#include <chrono>
#include <assert.h>
#include "matmul.h"
using namespace std;
__global__ void Kernel(const int* const matrixA, const int* const matrixB, int* const matrixC, const int n);
void allocateDeviceMemory(void** M, int size)
{
  cudaError_t err = cudaMalloc(M, size);
  assert(err==cudaSuccess);
}

void deallocateDeviceMemory(void* M)
{
  cudaError_t err = cudaFree(M);
  assert(err==cudaSuccess);
}

void matmul_ref(const int* const matrixA, const int* const matrixB,
                int* const matrixC, const int n) {
  // You can assume matrixC is initialized with zero
  for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++)
      for (int k = 0; k < n; k++)
        matrixC[i * n + j] += matrixA[i * n + k] * matrixB[k * n + j];
}

void matmul_optimized(const int* const matrixA, const int* const matrixB,
                      int* const matrixC, const int* const d_A, const int* const d_B,  int* const d_C, const int n) {

  // TODO: Implement your CUDA code
  #define TILE_WIDTH 32
  dim3 dimGrid(ceil((float)n/(float)TILE_WIDTH), ceil((float)n/(float)TILE_WIDTH), 1);
  dim3 dimBlock(TILE_WIDTH/2, TILE_WIDTH/2, 1);

  cudaMemcpy((void*)d_A, matrixA, sizeof(int)*n*n, cudaMemcpyHostToDevice);
  cudaMemcpy((void*)d_B, matrixB, sizeof(int)*n*n, cudaMemcpyHostToDevice);
  Kernel<<<dimGrid, dimBlock>>>(d_A, d_B, d_C, n);
  cudaMemcpy(matrixC, d_C, sizeof(int)*n*n, cudaMemcpyDeviceToHost);
}

__global__ void Kernel(const int* const matrixA, const int* const matrixB, int* const matrixC, const int n){
  // calculate 4 elements for each thread for speed up, similar to loop unrolling
  // (row,col), (row+TILE_SIZE/2, col), (row, col+TILE_SIZE/2), (row + TILE_SIZE/2, col + TILE_SIZE/2)
  
  // type def
  int width = n;
  int bx = blockIdx.x;  int by = blockIdx.y;
  int tx = threadIdx.x; int ty = threadIdx.y;
  
  // shared memory
  __shared__ int subTileA[TILE_WIDTH][TILE_WIDTH];
  __shared__ int subTileB[TILE_WIDTH][TILE_WIDTH];

  // row, col
  int row = by * TILE_WIDTH + ty;
  int col = bx * TILE_WIDTH + tx;

  // local sum
  int sum1=0;
  int sum2=0;
  int sum3=0;
  int sum4=0;
  
  // stride
  int diff= TILE_WIDTH / 2;

  // calculate
  for(int l=0; l < ceil((float)width/(float)TILE_WIDTH); ++l){
    // Load shared memory
    // if subTile is out of original matrix, set 0

    // Make SubTileA
    if(row < width && l * TILE_WIDTH + tx < width)
      subTileA[ty][tx] = matrixA[row * width + l * TILE_WIDTH + tx];
    else subTileA[ty][tx] = 0;

    if(row < width && l * TILE_WIDTH + tx + diff < width)
      subTileA[ty][tx + diff] = matrixA[row * width + l * TILE_WIDTH + tx + diff];
    else subTileA[ty][tx + diff] = 0;

    if(row + diff < width && l * TILE_WIDTH + tx < width)
      subTileA[ty + diff][tx] = matrixA[(row + diff) * width + l * TILE_WIDTH + tx];
    else subTileA[ty + diff][tx] = 0;

    if(row + diff < width && l * TILE_WIDTH + tx + diff < width)
      subTileA[ty + diff][tx + diff] = matrixA[(row + diff) * width + l * TILE_WIDTH + tx + diff];
    else subTileA[ty + diff][tx + diff] = 0;

    // Make SubTileB
    if(l * TILE_WIDTH + ty < width && col < width)
      subTileB[ty][tx] = matrixB[(l * TILE_WIDTH + ty) * width + col];
    else subTileB[ty][tx] = 0;

    if(l * TILE_WIDTH + ty < width && col + diff < width)
      subTileB[ty][tx + diff] = matrixB[(l * TILE_WIDTH + ty) * width + col + diff];
    else subTileB[ty][tx + diff] = 0;

    if(l * TILE_WIDTH + ty + diff < width && col < width)
      subTileB[ty + diff][tx] = matrixB[(l * TILE_WIDTH + ty + diff) * width + col];
    else subTileB[ty + diff][tx] = 0;

    if(l * TILE_WIDTH + ty + diff < width && col + diff < width)
      subTileB[ty + diff][tx + diff] = matrixB[(l * TILE_WIDTH + ty + diff) * width + col + diff];
    else subTileB[ty + diff][tx + diff] = 0;

    // synchronize threads
    __syncthreads();

    // Computation
    for(int k=0; k < TILE_WIDTH; ++k){
      sum1 += subTileA[ty][k] * subTileB[k][tx];
      sum2 += subTileA[ty][k] * subTileB[k][tx+diff];
      sum3 += subTileA[ty+diff][k] * subTileB[k][tx];
      sum4 += subTileA[ty+diff][k] * subTileB[k][tx+diff];
    }
    __syncthreads();
  }

  // Write Back
  if(row < width && col < width)
    matrixC[row * width + col] = sum1;
  if(row < width && col + diff < width)
    matrixC[row * width + col + diff] = sum2;
  if(row + diff < width && col < width)
    matrixC[(row + diff) * width + col] = sum3;
  if(row + diff < width && col + diff < width)
    matrixC[(row + diff) * width + col + diff] = sum4;
  
}
