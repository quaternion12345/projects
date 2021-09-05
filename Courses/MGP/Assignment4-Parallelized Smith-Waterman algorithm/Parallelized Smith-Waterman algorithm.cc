#include "smith_waterman_parallel.h"
//#include <algorithm>
#include "utils.h"
#include <stdio.h>
#include <vector>
#include <iostream>
using namespace Algorithms;

SmithWatermanParallel::SmithWatermanParallel(int seq1Length, int seq2Length, char* seq1, char* seq2, int gapOp, int gapEx):SimilarityAlgorithmParallel(seq1Length, seq2Length), gapOp(gapOp), gapEx(gapEx)
{
  
  A = new int*[seq1Length + 1];
  E = new int*[seq1Length + 1]; //left matrix
  F = new int*[seq1Length + 1]; //up matrix
  B = new BackUpStruct*[seq1Length + 1];

  A[0] = new int[(seq1Length + 1) * (seq2Length + 1)];
  E[0] = new int[(seq1Length + 1) * (seq2Length + 1)];
  F[0] = new int[(seq1Length + 1) * (seq2Length + 1)];
  B[0] = new BackUpStruct[(seq1Length + 1) * (seq2Length + 1)];

  for (int i = 1; i < seq1Length + 1; i++)
  {
    A[i] = A[0] + (seq2Length + 1)*i;
    E[i] = E[0] + (seq2Length + 1)*i;
    F[i] = F[0] + (seq2Length + 1)*i;
    B[i] = B[0] + (seq2Length + 1)*i;
  }
  
  /*
  // new address transformed matrix
  A = new int*[seq1Length + seq2Length + 1];
  E = new int*[seq1Length + seq2Length + 1];
  F = new int*[seq1Length + seq2Length + 1];
  B = new BackUpStruct*[seq1Length + seq2Length + 1];
  A[0] = new int[(seq1Length + 1) * (seq1Length + seq2Length + 1)];
  E[0] = new int[(seq1Length + 1) * (seq1Length + seq2Length + 1)];
  F[0] = new int[(seq1Length + 1) * (seq1Length + seq2Length + 1)];
  B[0] = new BackUpStruct[(seq1Length + 1) * (seq1Length + seq2Length + 1)];
  for (int i=1; i < seq1Length + seq2Length + 1; i++)
  {
    A[i] = A[0] + (seq1Length + 1)*i;
    E[i] = E[0] + (seq1Length + 1)*i;
    F[i] = F[0] + (seq1Length + 1)*i;
    B[i] = B[0] + (seq1Length + 1)*i;
  }
  */
  
  setSeq1(seq1, seq1Length);
  setSeq2(seq2, seq2Length);
}

int SmithWatermanParallel::matchMissmatchScore(char a, char b) {
  if (a == b)
    return matchScore;
  else
    return missmatchScore;
}  /* End of matchMissmatchScore */


void SmithWatermanParallel::FillCell(int i, int j)
{
      //printf("at %d, %d = %c %c\n", i, j, seq1[i-1], seq2[j-1]);
      
      E[i][j] = MAX(E[i][j - 1] - gapEx, A[i][j - 1] - gapOp);
      B[i][j - 1].continueLeft = (E[i][j] == E[i][j - 1] - gapEx);
      F[i][j] = MAX(F[i - 1][j] - gapEx, A[i - 1][j] - gapOp);
      B[i - 1][j].continueUp = (F[i][j] == F[i - 1][j] - gapEx);

      A[i][j] = MAX3(E[i][j], F[i][j], A[i - 1][j - 1] + matchMissmatchScore(seq1[i-1], seq2[j-1]));
      A[i][j] = MAX(A[i][j], 0);


      if (A[i][j] == 0)
        B[i][j].backDirection = stop; //SPECYFIC FOR SMITH WATERMAN
      else if(A[i][j] == (A[i - 1][j - 1] + matchMissmatchScore(seq1[i-1], seq2[j-1])))
        B[i][j].backDirection = crosswise;
      else if(A[i][j] == E[i][j])
        B[i][j].backDirection = left;
      else //if(A[i][j] == F[i][j])
        B[i][j].backDirection = up;


      if(A[i][j] > maxVal)
      {
        maxX = j;
        maxY = i;
        maxVal = A[i][j];
      }
      else if(A[i][j] == maxVal)
      {
	if(maxY > i){ maxX=j; maxY=i;}
	else if(maxY == i && maxX > j) maxX=j;
      }
      
      /*
      //down (i, j-1)
      E[i][j] = MAX(E[i-1][j] - gapEx, A[i-1][j] - gapOp);
      B[i - 1][j].continueLeft = (E[i][j] == E[i - 1][j] - gapEx);
      //diagonal (i-1, j)
      F[i][j] = MAX(F[i - 1][j - 1] - gapEx, A[i - 1][j - 1] - gapOp);
      B[i - 1][j - 1].continueUp = (F[i][j] == F[i - 1][j - 1] - gapEx);

      A[i][j] = MAX3(E[i][j], F[i][j], A[i - 2][j - 1] + matchMissmatchScore(seq1[j-1], seq2[i-j-1]));
      A[i][j] = MAX(A[i][j], 0);

      if (A[i][j] == 0)
        B[i][j].backDirection = stop; //SPECYFIC FOR SMITH WATERMAN
      else if(A[i][j] == (A[i - 2][j - 1] + matchMissmatchScore(seq1[j-1], seq2[i-j-1])))
        B[i][j].backDirection = crosswise;
      else if(A[i][j] == E[i][j])
        B[i][j].backDirection = left;
      else //if(A[i][j] == F[i][j])
        B[i][j].backDirection = up;

      if(A[i][j] > maxVal)
      {
        maxX = j;
	maxY = i;
	maxVal = A[i][j];
      }
      */
    }

std::vector<std::pair<int, int> > calc_pair(int d, int row, int column){
  // (1, d-1) ~ (d-1,1)
  std::vector<std::pair<int, int> > temp;
  for(int i=1; i<=row; ++i){
    if(d-i > column) continue;
    if(d-i < 1) break;
    temp.push_back(std::make_pair(i,d-i));
  }
  return temp;
}
void SmithWatermanParallel::FillMatrices()
{
  /*
   *   s e q 2
   * s
   * e
   * q
   * 1
   */
  //E - responsible for left direction
  //F - responsible for up   direction

  maxVal = INT_MIN;

  int blk_size = 32; // block size
  int row_cut = seq1Length - (seq1Length % blk_size); // last index of row block
  int column_cut = seq2Length - (seq2Length % blk_size); // last index of column block
  int row_block_num = seq1Length / blk_size; // number of blocks in row
  int column_block_num = seq2Length / blk_size; // number of blocks in column
  std::vector<std::pair<int, int> > diagonals; // temporal diagonal coordinate pairs
  for(int d=2; d <= row_block_num + column_block_num; ++d){
    diagonals = calc_pair(d, row_block_num, column_block_num); // find coordinate i+j = d
    #pragma omp parallel for
    for(int p=0; p<diagonals.size(); p++){
      // block indices to real indices
      int one = (diagonals[p].first - 1) * blk_size + 1; 
      int two = (diagonals[p].second - 1) * blk_size + 1;
      // sequential works inside the block 
      for(int i=0; i<blk_size; ++i){
        for(int j=0; j<blk_size; ++j){
	  FillCell(one+i, two+j);
	}
      }
    }
  }
  // process remainder blocks
  bool flag1 = (seq1Length % blk_size > 0);
  bool flag2 = (seq2Length % blk_size > 0);
  if(flag1){ // row_cut+1~seq1Length * 1~column_cut
    for(int i=row_cut+1; i<=seq1Length; ++i){
      for(int j=1; j<=column_cut; ++j){
	FillCell(i,j);
      }
    }
  }
  if(flag2){ // 1~row_cut * column_cut+1~seq2Length
    for(int i=1; i<=row_cut; ++i){
      for(int j=column_cut+1; j<=seq2Length; ++j){
	FillCell(i,j);
      }
    }
  }
  if(flag1 && flag2){ // row_cut+1~seq1Length * column_cut+1~seq2Length
    for(int i=row_cut+1; i<=seq1Length; ++i){
      for(int j=column_cut+1; j<=seq2Length; ++j){
	FillCell(i,j);
      }
    }
  }

  /*
  //parallelize diagonal
  for(int d = 2; d <= seq1Length + seq2Length; ++d){
      #pragma omp parallel for schedule(guided, 400)
      for(int j = 1; j <= seq1Length; ++j){
	  if(d < seq2Length+1 && j >= d) continue;
	  if(d > seq2Length+1 && j <= (d - seq2Length-1)) continue;
          FillCell(d,j);
      }
  }
  printf("maxY %d maxX %d maxVal %d\n", maxX, maxY-maxX, maxVal);
  */
  printf("maxY %d maxX %d maxVal %d\n", maxY, maxX, maxVal);
}

void SmithWatermanParallel::BackwardMoving()
{
  //BACKWARD MOVING
  int carret = 0;

  int y = maxY;
  int x = maxX;

  BackDirection prev = crosswise;
  while(B[y][x].backDirection != stop)
  {
    path.push_back(std::make_pair(y, x)); // x,y-x
    if (prev == up && B[y][x].continueUp) //CONTINUE GOING UP
    {                                          //GAP EXTENSION
      carret++;
      y--;
      //y--;
      //x--;
    }
    else if (prev == left && B[y][x].continueLeft) //CONTINUE GOING LEFT
    {                                         //GAP EXTENSION
      carret++;
      x--;
      //y--;
    }
    else
    {
      prev = B[y][x].backDirection;
      if(prev == up)
      {
        carret++;
	y--;
        //y--;
	//x--;
      }
      else if(prev == left)
      {
        carret++;
	x--;
        //y--;
      }
      else //prev == crosswise
      {
        carret++;
	x--;
	y--;
        //y-=2;
        //x--;
      }
    }
  }
}
