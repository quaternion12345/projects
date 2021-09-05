#include "vgg16_cuda.h"
__global__ void normalize(const uint8_t* const image, float* input, int B, int C, int H, int W){
  float max_int = 255.0L;
  float mean = 0.5L;
  float var = 0.5L;
  int b, c, h, w, i;
  b = blockIdx.x;
  c = blockIdx.y;
  h = threadIdx.y;
  w = threadIdx.x;
  i = b * (C*H*W) + c * (H*W) + h * (W) + w;
  if(h < H && w < W && i < B * C * H * W)
    input[i] = (image[i] / max_int - mean) / var;
}

__global__ void pad(float* input, float* input_padded, int B, int C, int H, int W, int P){
  int H_OUT = H+2*P;
  int W_OUT = W+2*P;
  int b, c, h, w, i, j;
  b = blockIdx.x;
  c = blockIdx.y;
  h = threadIdx.y;
  w = threadIdx.x;
  i = b * (C*H*W) + c * (H*W) + h * (W) + w;
  j = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + (h+P) * (W_OUT) + (w+P);
  if(h < H && w < W && i < B * C * H * W)
    input_padded[j] = input[i];
}

__global__ void pad5(float* input, float* input_padded, int B, int C, int H, int W, int P){
  // output shape (4x4)
  // 0 0 0 0
  // 0 1 2 0
  // 0 3 4 0
  // 0 0 0 0
  int H_OUT = H+2*P;
  int W_OUT = W+2*P;
  int b, c;
  b = blockIdx.x;
  c = threadIdx.x;
  int i1 = b * (C*H*W) + c * (H*W) + 0 * (W) + 0;
  int i2 = b * (C*H*W) + c * (H*W) + 0 * (W) + 1;
  int i3 = b * (C*H*W) + c * (H*W) + 1 * (W) + 0;
  int i4 = b * (C*H*W) + c * (H*W) + 1 * (W) + 1;
  int j1 = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + (0+P) * (W_OUT) + (0+P);
  int j2 = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + (0+P) * (W_OUT) + (1+P);
  int j3 = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + (1+P) * (W_OUT) + (0+P);
  int j4 = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + (1+P) * (W_OUT) + (1+P);
  input_padded[j1] = input[i1];
  input_padded[j2] = input[i2];
  input_padded[j3] = input[i3];
  input_padded[j4] = input[i4];
}
template <int InputChannel>
__global__ void conv(float* input, float* output, float* weight, float* bias, int B, int H, int W, int IC, int OC, int K){
  int H_OUT = H - (K-1);
  int W_OUT = W - (K-1);
  int b, h, w, oc, j;
  b = blockIdx.x;
  oc = blockIdx.y;
  h = threadIdx.y;
  w = threadIdx.x;
  j = b * (OC * H_OUT * W_OUT) + oc * (H_OUT * W_OUT) + h * W_OUT + w;
  if(h < H_OUT && w < W_OUT && j < B * OC * H_OUT * W_OUT){
    float temp = bias[oc];
    if(InputChannel == 3){
    for(int ic=0; ic<3; ic++){
      int input_base = b * (IC * H * W) + ic * (H * W) + h * (W) + w;
      int kernel_base = oc * (IC * K * K) + ic * (K * K);
      for(int kh=0; kh<3; kh++){
	for(int kw=0; kw<3; kw++){
          temp += input[input_base + kh * (W) + kw] * weight[kernel_base + kh * (K) + kw];
	}
      }
    }
    }
    if(InputChannel == 64){
    for(int ic=0; ic<64; ic++){
      int input_base = b * (IC * H * W) + ic * (H * W) + h * (W) + w;
      int kernel_base = oc * (IC * K * K) + ic * (K * K);
      for(int kh=0; kh<3; kh++){
	for(int kw=0; kw<3; kw++){
          temp += input[input_base + kh * (W) + kw] * weight[kernel_base + kh * (K) + kw];
	}
      }
    }
    }
    if(InputChannel == 128){
    for(int ic=0; ic<128; ic++){
      int input_base = b * (IC * H * W) + ic * (H * W) + h * (W) + w;
      int kernel_base = oc * (IC * K * K) + ic * (K * K);
      for(int kh=0; kh<3; kh++){
	for(int kw=0; kw<3; kw++){
          temp += input[input_base + kh * (W) + kw] * weight[kernel_base + kh * (K) + kw];
	}
      }
    }
    }
    if(InputChannel == 256){
    for(int ic=0; ic<256; ic++){
      int input_base = b * (IC * H * W) + ic * (H * W) + h * (W) + w;
      int kernel_base = oc * (IC * K * K) + ic * (K * K);
      for(int kh=0; kh<3; kh++){
	for(int kw=0; kw<3; kw++){
          temp += input[input_base + kh * (W) + kw] * weight[kernel_base + kh * (K) + kw];
	}
      }
    }
    }
    if(InputChannel == 512){
    for(int ic=0; ic<512; ic++){
      int input_base = b * (IC * H * W) + ic * (H * W) + h * (W) + w;
      int kernel_base = oc * (IC * K * K) + ic * (K * K);
      for(int kh=0; kh<3; kh++){
	for(int kw=0; kw<3; kw++){
          temp += input[input_base + kh * (W) + kw] * weight[kernel_base + kh * (K) + kw];
	}
      }
    }
    }
    output[j] = (temp > (float)0.0 ? temp : (float)0.0); // relu
  }
}

__global__ void conv5(float* input, float* output, float* weight, float* bias, int B, int H, int W, int IC, int OC, int K){
  // output shape(2x2)
  // 1 2
  // 3 4
  int H_OUT = H - (K-1);
  int W_OUT = W - (K-1);
  int b, oc;
  b = blockIdx.x;
  oc = threadIdx.x;
  float temp1 = bias[oc];
  float temp2 = bias[oc];
  float temp3 = bias[oc];
  float temp4 = bias[oc];
  int ob1 = b * (OC * H_OUT * W_OUT) + oc * (H_OUT * W_OUT) + 0 * W_OUT + 0;
  int ob2 = b * (OC * H_OUT * W_OUT) + oc * (H_OUT * W_OUT) + 0 * W_OUT + 1;
  int ob3 = b * (OC * H_OUT * W_OUT) + oc * (H_OUT * W_OUT) + 1 * W_OUT + 0;
  int ob4 = b * (OC * H_OUT * W_OUT) + oc * (H_OUT * W_OUT) + 1 * W_OUT + 1;
  for(int ic=0; ic<512; ic++){
    int ib1 = b * (IC * H * W) + ic * (H * W) + 0 * (W) + 0;
    int ib2 = b * (IC * H * W) + ic * (H * W) + 0 * (W) + 1;
    int ib3 = b * (IC * H * W) + ic * (H * W) + 1 * (W) + 0;
    int ib4 = b * (IC * H * W) + ic * (H * W) + 1 * (W) + 1;
    int kb = oc * (IC * K * K) + ic * (K * K);
    for(int kh=0; kh<3; kh++){
      for(int kw=0; kw<3; kw++){
	temp1 += input[ib1 + kh * (W) + kw] * weight[kb + kh * (K) + kw];
	temp2 += input[ib2 + kh * (W) + kw] * weight[kb + kh * (K) + kw];
	temp3 += input[ib3 + kh * (W) + kw] * weight[kb + kh * (K) + kw];
	temp4 += input[ib4 + kh * (W) + kw] * weight[kb + kh * (K) + kw];
      }
    }
  }
  output[ob1] = (temp1 > (float)0.0 ? temp1 : (float)0.0); // relu
  output[ob2] = (temp2 > (float)0.0 ? temp2 : (float)0.0); // relu
  output[ob3] = (temp3 > (float)0.0 ? temp3 : (float)0.0); // relu
  output[ob4] = (temp4 > (float)0.0 ? temp4 : (float)0.0); // relu
}

__global__ void pool(float* input, float* output, int B, int C, int H, int W){
  int scale = 2;
  int H_OUT = H / scale;
  int W_OUT = W / scale;
  int b, c, h, w, i, j;
  b = blockIdx.x;
  c = blockIdx.y;
  h = threadIdx.y;
  w = threadIdx.x;
  // i = b * (C*H*W) + c * (H*W) + h * (W) + w;
  j = b * (C*H_OUT*W_OUT) + c * (H_OUT*W_OUT) + h * W_OUT + w; //output base index
  if(h < H_OUT && w < W_OUT && j < B * C * H_OUT * W_OUT){
    float max_val = -255.0;
    for(int sh=0; sh<2; sh++){
      for(int sw=0; sw<2; sw++){
	i = b * (C*H*W) + c * (H*W) + (2*h+sh) * (W) + (2*w+sw); // input base index
        float val = input[i];
	if(val - max_val > (float)0.0)
	  max_val = val;
      }
    }
    output[j] = max_val;
  }
}

__global__ void pool5(float* input, float* output, int B, int C, int H, int W){
  // output shape(1x1)
  // 1
  int scale = 2;
  int H_OUT = H / scale;
  int W_OUT = W / scale;
  int b, c, i, j;
  b = blockIdx.x;
  c = threadIdx.x;
  j = b * (C * H_OUT * W_OUT) + c * (H_OUT * W_OUT);
  float max_val = -255.0;
  for(int sh=0; sh<2; sh++){
    for(int sw=0; sw<2; sw++){
      i = b * (C*H*W) + c * (H*W) + (sh) * (W) + (sw);
      float val = input[i];
      if(val - max_val > (float)0.0)
	max_val = val;
    }
  }
  output[j] = max_val;
}

__global__ void fc(float* input, float* output, float* weight, float* bias, int B, int IC, int OC){
  // 512 --> 10
  int b, oc;
  b = blockIdx.x;
  oc = threadIdx.x;
  if(b * OC + oc < B * OC){
    float temp = bias[oc];
    for(int ic=0; ic<512; ic++)
      temp += weight[oc * IC + ic] * input[b * IC + ic];
    output[b * OC + oc] = temp;
  }
}


void vgg16_cuda::predict(int batch) {
    // Grid: [# of batch, # of output channel, # of tile]
    // Block:[x_size, y_size]
    // ReLU is included in convolution layer
    //#define TILE_WIDTH 8
    
    dim3 dimGrid(batch, input_channel); // 128, 3
    dim3 dimBlock(32, 32);
    normalize<<<dimGrid, dimBlock>>>(d_image, d_input, batch, input_channel, input_size, input_size);
  
    //////////BLOCK 1/////////////////////////////////
    // TODO: Implement pad
    cudaMemset(d_input_padded, 0, sizeof(d_input_padded));
    pad<<<dimGrid, dimBlock>>>(d_input, d_input_padded, batch, input_channel, input_size, input_size, conv1_1_padding_size);

    // TODO: Implement conv1_1
    //int tile1 = (input_size / TILE_WIDTH) * (input_size / TILE_WIDTH);
    dim3 dimGrid11(batch, conv1_1_out_channel); // 128, 64
    dim3 dimBlock11(input_size, input_size); // 32, 32
    conv<3><<<dimGrid11, dimBlock11>>>(d_input_padded, d_C1_1_feature_map, d_conv1_1_weight, d_conv1_1_bias, batch, input_size+2*conv1_1_padding_size, input_size+2*conv1_1_padding_size, conv1_1_in_channel, conv1_1_out_channel, conv1_1_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C1_1_feature_map_padded, 0, sizeof(d_C1_1_feature_map_padded));
    pad<<<dimGrid11, dimBlock11>>>(d_C1_1_feature_map, d_C1_1_feature_map_padded, batch, C1_1_channel, C1_1_size, C1_1_size, conv1_2_padding_size);

    // TODO: Implement conv1_2
    dim3 dimGrid12(batch, conv1_2_out_channel); // 128, 64
    dim3 dimBlock12(C1_1_size, C1_1_size); // 32, 32
    conv<64><<<dimGrid12, dimBlock12>>>(d_C1_1_feature_map_padded, d_C1_2_feature_map, d_conv1_2_weight, d_conv1_2_bias, batch, C1_1_size+2*conv1_2_padding_size, C1_1_size+2*conv1_2_padding_size, conv1_2_in_channel, conv1_2_out_channel, conv1_2_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pool
    dim3 dimGrid1p(batch, C1_2_channel); // 128, 64
    dim3 dimBlock1p(16, 16);
    pool<<<dimGrid1p, dimBlock1p>>>(d_C1_2_feature_map, d_S1_feature_map, batch, C1_2_channel, C1_2_size, C1_2_size);
  
    //////////BLOCK 2/////////////////////////////////
    // TODO: Implement pad
    cudaMemset(d_S1_feature_map_padded, 0, sizeof(d_S1_feature_map_padded));	    
    pad<<<dimGrid1p, dimBlock1p>>>(d_S1_feature_map, d_S1_feature_map_padded, batch, S1_channel, S1_size, S1_size, conv2_1_padding_size);

    // TODO: Implement conv2_1
    //int tile2 = (S1_size / TILE_WIDTH) * (S1_size / TILE_WIDTH);
    dim3 dimGrid21(batch, conv2_1_out_channel); // 128, 128
    dim3 dimBlock21(S1_size, S1_size); // 16, 16
    conv<64><<<dimGrid21, dimBlock21>>>(d_S1_feature_map_padded, d_C2_1_feature_map, d_conv2_1_weight, d_conv2_1_bias, batch, S1_size+2*conv2_1_padding_size, S1_size+2*conv2_1_padding_size, conv2_1_in_channel, conv2_1_out_channel, conv2_1_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C2_1_feature_map_padded, 0, sizeof(d_C2_1_feature_map_padded));
    pad<<<dimGrid21, dimBlock21>>>(d_C2_1_feature_map, d_C2_1_feature_map_padded, batch, C2_1_channel, C2_1_size, C2_1_size, conv2_2_padding_size);

    // TODO: Implement conv2_2
    dim3 dimGrid22(batch, conv2_2_out_channel); // 128, 128
    dim3 dimBlock22(C2_1_size, C2_1_size); // 16, 16
    conv<128><<<dimGrid22, dimBlock22>>>(d_C2_1_feature_map_padded, d_C2_2_feature_map, d_conv2_2_weight, d_conv2_2_bias, batch, C2_1_size+2*conv2_2_padding_size, C2_1_size+2*conv2_2_padding_size, conv2_2_in_channel, conv2_2_out_channel, conv2_2_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pool
    dim3 dimGrid2p(batch, C2_2_channel); // 128, 128
    dim3 dimBlock2p(8, 8);
    pool<<<dimGrid2p, dimBlock2p>>>(d_C2_2_feature_map, d_S2_feature_map, batch, C2_2_channel, C2_2_size, C2_2_size);
   
    //////////BLOCK 3/////////////////////////////////
    // TODO: Implement pad
    cudaMemset(d_S2_feature_map_padded, 0, sizeof(d_S2_feature_map_padded));
    pad<<<dimGrid2p, dimBlock2p>>>(d_S2_feature_map, d_S2_feature_map_padded, batch, S2_channel, S2_size, S2_size, conv3_1_padding_size);

    // TODO: Implement conv3_1
    dim3 dimGrid31(batch, conv3_1_out_channel); // 128, 256
    dim3 dimBlock31(8, 8);
    conv<128><<<dimGrid31, dimBlock31>>>(d_S2_feature_map_padded, d_C3_1_feature_map, d_conv3_1_weight, d_conv3_1_bias, batch, S2_size+2*conv3_1_padding_size, S2_size+2*conv3_1_padding_size, conv3_1_in_channel, conv3_1_out_channel, conv3_1_kernel_size);
    
    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C3_1_feature_map_padded, 0, sizeof(d_C3_1_feature_map_padded));
    pad<<<dimGrid31, dimBlock31>>>(d_C3_1_feature_map, d_C3_1_feature_map_padded, batch, C3_1_channel, C3_1_size, C3_1_size, conv3_2_padding_size);

    // TODO: Implement conv3_2
    dim3 dimGrid32(batch, conv3_2_out_channel); // 128, 256
    dim3 dimBlock32(8, 8);
    conv<256><<<dimGrid32, dimBlock32>>>(d_C3_1_feature_map_padded, d_C3_2_feature_map, d_conv3_2_weight, d_conv3_2_bias, batch, C3_1_size+2*conv3_2_padding_size, C3_1_size+2*conv3_2_padding_size, conv3_2_in_channel, conv3_2_out_channel, conv3_2_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C3_2_feature_map_padded, 0, sizeof(d_C3_2_feature_map_padded));
    pad<<<dimGrid32, dimBlock32>>>(d_C3_2_feature_map, d_C3_2_feature_map_padded, batch, C3_2_channel, C3_2_size, C3_2_size, conv3_3_padding_size);

    // TODO: Implement conv3_3
    dim3 dimGrid33(batch, conv3_3_out_channel); // 128, 256
    dim3 dimBlock33(8, 8);
    conv<256><<<dimGrid33, dimBlock33>>>(d_C3_2_feature_map_padded, d_C3_3_feature_map, d_conv3_3_weight, d_conv3_3_bias, batch, C3_2_size+2*conv3_3_padding_size, C3_2_size+2*conv3_3_padding_size, conv3_3_in_channel, conv3_3_out_channel, conv3_3_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pool
    dim3 dimGrid3p(batch, C3_3_channel); // 128, 256
    dim3 dimBlock3p(4, 4);
    pool<<<dimGrid3p, dimBlock3p>>>(d_C3_3_feature_map, d_S3_feature_map, batch, C3_3_channel, C3_3_size, C3_3_size);
   
    //////////BLOCK 4/////////////////////////////////
    // TODO: Implement pad
    cudaMemset(d_S3_feature_map_padded, 0, sizeof(d_S3_feature_map_padded));
    pad<<<dimGrid3p, dimBlock3p>>>(d_S3_feature_map, d_S3_feature_map_padded, batch, S3_channel, S3_size, S3_size, conv4_1_padding_size);
    
    // TODO: Implement conv4_1
    dim3 dimGrid41(batch, conv4_1_out_channel); // 128, 512
    dim3 dimBlock41(4, 4);
    conv<256><<<dimGrid41, dimBlock41>>>(d_S3_feature_map_padded, d_C4_1_feature_map, d_conv4_1_weight, d_conv4_1_bias, batch, S3_size+2*conv4_1_padding_size, S3_size+2*conv4_1_padding_size, conv4_1_in_channel, conv4_1_out_channel, conv4_1_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C4_1_feature_map_padded, 0, sizeof(d_C4_1_feature_map_padded));
    pad<<<dimGrid41, dimBlock41>>>(d_C4_1_feature_map, d_C4_1_feature_map_padded, batch, C4_1_channel, C4_1_size, C4_1_size, conv4_2_padding_size);

    // TODO: Implement conv4_2
    dim3 dimGrid42(batch, conv4_2_out_channel); // 128, 512
    dim3 dimBlock42(4, 4);
    conv<512><<<dimGrid42, dimBlock42>>>(d_C4_1_feature_map_padded, d_C4_2_feature_map, d_conv4_2_weight, d_conv4_2_bias, batch, C4_1_size+2*conv4_2_padding_size, C4_1_size+2*conv4_2_padding_size, conv4_2_in_channel, conv4_2_out_channel, conv4_2_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C4_2_feature_map_padded, 0, sizeof(d_C4_2_feature_map_padded));
    pad<<<dimGrid42, dimBlock42>>>(d_C4_2_feature_map, d_C4_2_feature_map_padded, batch, C4_2_channel, C4_2_size, C4_2_size, conv4_3_padding_size);

    // TODO: Implement conv4_3
    dim3 dimGrid43(batch, conv4_3_out_channel); // 128, 512
    dim3 dimBlock43(4, 4);
    conv<512><<<dimGrid43, dimBlock43>>>(d_C4_2_feature_map_padded, d_C4_3_feature_map, d_conv4_3_weight, d_conv4_3_bias, batch, C4_2_size+2*conv4_3_padding_size, C4_2_size+2*conv4_3_padding_size, conv4_3_in_channel, conv4_3_out_channel, conv4_3_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pool
    dim3 dimGrid4p(batch, C4_3_channel); // 128, 512
    dim3 dimBlock4p(2, 2);
    pool<<<dimGrid4p, dimBlock4p>>>(d_C4_3_feature_map, d_S4_feature_map, batch, C4_3_channel, C4_3_size, C4_3_size);
   
    //////////BLOCK 5/////////////////////////////////
    // TODO: Implement pad
    cudaMemset(d_S4_feature_map_padded, 0, sizeof(d_S4_feature_map_padded));
    pad5<<<batch, C4_3_channel>>>(d_S4_feature_map, d_S4_feature_map_padded, batch, S4_channel, S4_size, S4_size, conv5_1_padding_size);

    // TODO: Implement conv5_1 [128x512x4x4] --> [128x512x2x2]
    conv5<<<batch, conv5_1_out_channel>>>(d_S4_feature_map_padded, d_C5_1_feature_map, d_conv5_1_weight, d_conv5_1_bias, batch, S4_size+2*conv5_1_padding_size, S4_size+2*conv5_1_padding_size, conv5_1_in_channel, conv5_1_out_channel, conv5_1_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C5_1_feature_map_padded, 0, sizeof(d_C5_1_feature_map_padded));
    pad5<<<batch, conv5_1_out_channel>>>(d_C5_1_feature_map, d_C5_1_feature_map_padded, batch, C5_1_channel, C5_1_size, C5_1_size, conv5_2_padding_size);

    // TODO: Implement conv5_2 [128x512x4x4] --> [128x512x2x2]
    conv5<<<batch, conv5_2_out_channel>>>(d_C5_1_feature_map_padded, d_C5_2_feature_map, d_conv5_2_weight, d_conv5_2_bias, batch, C5_1_size+2*conv5_2_padding_size, C5_1_size+2*conv5_2_padding_size, conv5_2_in_channel, conv5_2_out_channel, conv5_2_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pad
    cudaMemset(d_C5_2_feature_map_padded, 0, sizeof(d_C5_2_feature_map_padded));
    pad5<<<batch, conv5_2_out_channel>>>(d_C5_2_feature_map, d_C5_2_feature_map_padded, batch, C5_2_channel, C5_2_size, C5_2_size, conv5_3_padding_size);

    // TODO: Implement conv5_3
    conv5<<<batch, conv5_3_out_channel>>>(d_C5_2_feature_map_padded, d_C5_3_feature_map, d_conv5_3_weight, d_conv5_3_bias, batch, C5_2_size+2*conv5_3_padding_size, C5_2_size+2*conv5_3_padding_size, conv5_3_in_channel, conv5_3_out_channel, conv5_3_kernel_size);

    // TODO: Implement relu
    // TODO: Implement pool // [128x512x2x2] --> [128x512x1x1]
    pool5<<<batch, C5_3_channel>>>(d_C5_3_feature_map, d_S5_feature_map, batch, C5_3_channel, C5_3_size, C5_3_size);
  
    // TODO: Implement fc1 [128x512] --> [128x10]
    fc<<<batch, fc1_out_channel>>>(d_S5_feature_map, d_output, d_fc1_weight, d_fc1_bias, batch, fc1_in_channel, fc1_out_channel);
    // TODO: Implement relu --> This step does not exist in cpu version, just skip this.

    /* NOTE: unless you want to make a major change to this class structure, 
    *  you need to write your output to the device memory d_output 
    *  so that classify() can handle the rest.
    */
}

void vgg16_cuda::prepare_device_memory(uint8_t* image) {
  // Alloc Model Parameters

  //////////BLOCK 1/////////////////////////////////
  cudaMalloc((void**)&d_conv1_1_weight,
             sizeof(float) * conv1_1_in_channel * conv1_1_out_channel *
                 conv1_1_kernel_size * conv1_1_kernel_size);
  cudaMalloc((void**)&d_conv1_1_bias, sizeof(float) * conv1_1_out_channel);
  cudaMalloc((void**)&d_conv1_2_weight,
             sizeof(float) * conv1_2_in_channel * conv1_2_out_channel *
                 conv1_2_kernel_size * conv1_2_kernel_size);
  cudaMalloc((void**)&d_conv1_2_bias, sizeof(float) * conv1_2_out_channel);

  //////////BLOCK 2/////////////////////////////////
  cudaMalloc((void**)&d_conv2_1_weight,
             sizeof(float) * conv2_1_in_channel * conv2_1_out_channel *
                 conv2_1_kernel_size * conv2_1_kernel_size);
  cudaMalloc((void**)&d_conv2_1_bias, sizeof(float) * conv2_1_out_channel);
  cudaMalloc((void**)&d_conv2_2_weight,
             sizeof(float) * conv2_2_in_channel * conv2_2_out_channel *
                 conv2_2_kernel_size * conv2_2_kernel_size);
  cudaMalloc((void**)&d_conv2_2_bias, sizeof(float) * conv2_2_out_channel);

  //////////BLOCK 3/////////////////////////////////
  cudaMalloc((void**)&d_conv3_1_weight,
             sizeof(float) * conv3_1_in_channel * conv3_1_out_channel *
                 conv3_1_kernel_size * conv3_1_kernel_size);
  cudaMalloc((void**)&d_conv3_1_bias, sizeof(float) * conv3_1_out_channel);
  cudaMalloc((void**)&d_conv3_2_weight,
             sizeof(float) * conv3_2_in_channel * conv3_2_out_channel *
                 conv3_2_kernel_size * conv3_2_kernel_size);
  cudaMalloc((void**)&d_conv3_2_bias, sizeof(float) * conv3_2_out_channel);
  cudaMalloc((void**)&d_conv3_3_weight,
             sizeof(float) * conv3_3_in_channel * conv3_3_out_channel *
                 conv3_3_kernel_size * conv3_3_kernel_size);
  cudaMalloc((void**)&d_conv3_3_bias, sizeof(float) * conv3_3_out_channel);

  //////////BLOCK 4/////////////////////////////////
  cudaMalloc((void**)&d_conv4_1_weight,
             sizeof(float) * conv4_1_in_channel * conv4_1_out_channel *
                 conv4_1_kernel_size * conv4_1_kernel_size);
  cudaMalloc((void**)&d_conv4_1_bias, sizeof(float) * conv4_1_out_channel);
  cudaMalloc((void**)&d_conv4_2_weight,
             sizeof(float) * conv4_2_in_channel * conv4_2_out_channel *
                 conv4_2_kernel_size * conv4_2_kernel_size);
  cudaMalloc((void**)&d_conv4_2_bias, sizeof(float) * conv4_2_out_channel);
  cudaMalloc((void**)&d_conv4_3_weight,
             sizeof(float) * conv4_3_in_channel * conv4_3_out_channel *
                 conv4_3_kernel_size * conv4_3_kernel_size);
  cudaMalloc((void**)&d_conv4_3_bias, sizeof(float) * conv4_3_out_channel);

  //////////BLOCK 5/////////////////////////////////
  cudaMalloc((void**)&d_conv5_1_weight,
             sizeof(float) * conv5_1_in_channel * conv5_1_out_channel *
                 conv5_1_kernel_size * conv5_1_kernel_size);
  cudaMalloc((void**)&d_conv5_1_bias, sizeof(float) * conv5_1_out_channel);
  cudaMalloc((void**)&d_conv5_2_weight,
             sizeof(float) * conv5_2_in_channel * conv5_2_out_channel *
                 conv5_2_kernel_size * conv5_2_kernel_size);
  cudaMalloc((void**)&d_conv5_2_bias, sizeof(float) * conv5_2_out_channel);
  cudaMalloc((void**)&d_conv5_3_weight,
             sizeof(float) * conv5_3_in_channel * conv5_3_out_channel *
                 conv5_3_kernel_size * conv5_3_kernel_size);
  cudaMalloc((void**)&d_conv5_3_bias, sizeof(float) * conv5_3_out_channel);

  //////////FC 1////////////////////////////////////
  cudaMalloc((void**)&d_fc1_weight,
             sizeof(float) * fc1_in_channel * fc1_out_channel);
  cudaMalloc((void**)&d_fc1_bias, sizeof(float) * fc1_out_channel);

  // Alloc Activations
  cudaMalloc((void**)&d_image,
             sizeof(uint8_t) * batch * input_size * input_size * input_channel);
  cudaMalloc((void**)&d_input,
             sizeof(float) * batch * input_channel * input_size * input_size);

  //////////BLOCK 1/////////////////////////////////
  cudaMalloc((void**)&d_input_padded,
             sizeof(float) * batch * input_channel * (input_size+2*conv1_1_padding_size) * (input_size+2*conv1_1_padding_size));
  cudaMalloc((void**)&d_C1_1_feature_map,
             sizeof(float) * batch * C1_1_channel * C1_1_size * C1_1_size);
  cudaMalloc((void**)&d_C1_1_feature_map_padded,
             sizeof(float) * batch * C1_1_channel * (C1_1_size+2*conv1_2_padding_size) * (C1_1_size+2*conv1_2_padding_size));
  cudaMalloc((void**)&d_C1_2_feature_map,
             sizeof(float) * batch * C1_2_channel * C1_2_size * C1_2_size);
  cudaMalloc((void**)&d_S1_feature_map,
             sizeof(float) * batch * S1_channel * S1_size * S1_size);

  //////////BLOCK 2/////////////////////////////////
  cudaMalloc((void**)&d_S1_feature_map_padded,
             sizeof(float) * batch * S1_channel * (S1_size+2*conv2_1_padding_size) * (S1_size+2*conv2_1_padding_size));
  cudaMalloc((void**)&d_C2_1_feature_map,
             sizeof(float) * batch * C2_1_channel * C2_1_size * C2_1_size);
  cudaMalloc((void**)&d_C2_1_feature_map_padded,
             sizeof(float) * batch * C2_1_channel * (C2_1_size+2*conv2_2_padding_size) * (C2_1_size+2*conv2_2_padding_size));
  cudaMalloc((void**)&d_C2_2_feature_map,
             sizeof(float) * batch * C2_2_channel * C2_2_size * C2_2_size);
  cudaMalloc((void**)&d_S2_feature_map,
             sizeof(float) * batch * S2_channel * S2_size * S2_size);

  //////////BLOCK 3/////////////////////////////////
  cudaMalloc((void**)&d_S2_feature_map_padded,
             sizeof(float) * batch * S2_channel * (S2_size+2*conv3_1_padding_size) * (S2_size+2*conv3_1_padding_size));
  cudaMalloc((void**)&d_C3_1_feature_map,
             sizeof(float) * batch * C3_1_channel * C3_1_size * C3_1_size);
  cudaMalloc((void**)&d_C3_1_feature_map_padded,
             sizeof(float) * batch * C3_1_channel * (C3_1_size+2*conv3_2_padding_size) * (C3_1_size+2*conv3_2_padding_size));
  cudaMalloc((void**)&d_C3_2_feature_map,
             sizeof(float) * batch * C3_2_channel * C3_2_size * C3_2_size);
  cudaMalloc((void**)&d_C3_2_feature_map_padded,
             sizeof(float) * batch * C3_2_channel * (C3_2_size+2*conv3_3_padding_size) * (C3_2_size+2*conv3_3_padding_size));
  cudaMalloc((void**)&d_C3_3_feature_map,
             sizeof(float) * batch * C3_3_channel * C3_3_size * C3_3_size);
  cudaMalloc((void**)&d_S3_feature_map,
             sizeof(float) * batch * S3_channel * S3_size * S3_size);

  //////////BLOCK 4/////////////////////////////////
  cudaMalloc((void**)&d_S3_feature_map_padded,
             sizeof(float) * batch * S3_channel * (S3_size+2*conv4_1_padding_size) * (S3_size+2*conv4_1_padding_size));
  cudaMalloc((void**)&d_C4_1_feature_map,
             sizeof(float) * batch * C4_1_channel * C4_1_size * C4_1_size);
  cudaMalloc((void**)&d_C4_1_feature_map_padded,
             sizeof(float) * batch * C4_1_channel * (C4_1_size+2*conv4_2_padding_size) * (C4_1_size+2*conv4_2_padding_size));
  cudaMalloc((void**)&d_C4_2_feature_map,
             sizeof(float) * batch * C4_2_channel * C4_2_size * C4_2_size);
  cudaMalloc((void**)&d_C4_2_feature_map_padded,
             sizeof(float) * batch * C4_2_channel * (C4_2_size+2*conv4_3_padding_size) * (C4_2_size+2*conv4_3_padding_size));
  cudaMalloc((void**)&d_C4_3_feature_map,
             sizeof(float) * batch * C4_3_channel * C4_3_size * C4_3_size);
  cudaMalloc((void**)&d_S4_feature_map,
             sizeof(float) * batch * S4_channel * S4_size * S4_size);

  //////////BLOCK 5/////////////////////////////////
  cudaMalloc((void**)&d_S4_feature_map_padded,
             sizeof(float) * batch * S4_channel * (S4_size+2*conv5_1_padding_size) * (S4_size+2*conv5_1_padding_size));
  cudaMalloc((void**)&d_C5_1_feature_map,
             sizeof(float) * batch * C5_1_channel * C5_1_size * C5_1_size);
  cudaMalloc((void**)&d_C5_1_feature_map_padded,
             sizeof(float) * batch * C5_1_channel * (C5_1_size+2*conv5_2_padding_size) * (C5_1_size+2*conv5_2_padding_size));
  cudaMalloc((void**)&d_C5_2_feature_map,
             sizeof(float) * batch * C5_2_channel * C5_2_size * C5_2_size);
  cudaMalloc((void**)&d_C5_2_feature_map_padded,
             sizeof(float) * batch * C5_2_channel * (C5_2_size+2*conv5_3_padding_size) * (C5_2_size+2*conv5_3_padding_size));
  cudaMalloc((void**)&d_C5_3_feature_map,
             sizeof(float) * batch * C5_3_channel * C5_3_size * C5_3_size);
  cudaMalloc((void**)&d_S5_feature_map,
             sizeof(float) * batch * S5_channel * S5_size * S5_size);


  cudaMalloc((void**)&d_output, sizeof(float) * batch * output_size);

  // Copy Parameters
  //////////BLOCK 1/////////////////////////////////
  cudaMemcpy(d_conv1_1_weight, conv1_1_weight,
             sizeof(float) * conv1_1_in_channel * conv1_1_out_channel *
                 conv1_1_kernel_size * conv1_1_kernel_size,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv1_1_bias, conv1_1_bias, sizeof(float) * conv1_1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv1_2_weight, conv1_2_weight,
              sizeof(float) * conv1_2_in_channel * conv1_2_out_channel *
                  conv1_2_kernel_size * conv1_2_kernel_size,
              cudaMemcpyHostToDevice);
   cudaMemcpy(d_conv1_2_bias, conv1_2_bias, sizeof(float) * conv1_2_out_channel,
              cudaMemcpyHostToDevice);

  //////////BLOCK 2/////////////////////////////////
  cudaMemcpy(d_conv2_1_weight, conv2_1_weight,
             sizeof(float) * conv2_1_in_channel * conv2_1_out_channel *
                 conv2_1_kernel_size * conv2_1_kernel_size,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv2_1_bias, conv2_1_bias, sizeof(float) * conv2_1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv2_2_weight, conv2_2_weight,
              sizeof(float) * conv2_2_in_channel * conv2_2_out_channel *
                  conv2_2_kernel_size * conv2_2_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv2_2_bias, conv2_2_bias, sizeof(float) * conv2_2_out_channel,
              cudaMemcpyHostToDevice);

  //////////BLOCK 3/////////////////////////////////
  cudaMemcpy(d_conv3_1_weight, conv3_1_weight,
             sizeof(float) * conv3_1_in_channel * conv3_1_out_channel *
                 conv3_1_kernel_size * conv3_1_kernel_size,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv3_1_bias, conv3_1_bias, sizeof(float) * conv3_1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv3_2_weight, conv3_2_weight,
              sizeof(float) * conv3_2_in_channel * conv3_2_out_channel *
                  conv3_2_kernel_size * conv3_2_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv3_2_bias, conv3_2_bias, sizeof(float) * conv3_2_out_channel,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv3_3_weight, conv3_3_weight,
              sizeof(float) * conv3_3_in_channel * conv3_3_out_channel *
                  conv3_3_kernel_size * conv3_3_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv3_3_bias, conv3_3_bias, sizeof(float) * conv3_3_out_channel,
              cudaMemcpyHostToDevice);

  //////////BLOCK 4/////////////////////////////////
  cudaMemcpy(d_conv4_1_weight, conv4_1_weight,
             sizeof(float) * conv4_1_in_channel * conv4_1_out_channel *
                 conv4_1_kernel_size * conv4_1_kernel_size,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv4_1_bias, conv4_1_bias, sizeof(float) * conv4_1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv4_2_weight, conv4_2_weight,
              sizeof(float) * conv4_2_in_channel * conv4_2_out_channel *
                  conv4_2_kernel_size * conv4_2_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv4_2_bias, conv4_2_bias, sizeof(float) * conv4_2_out_channel,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv4_3_weight, conv4_3_weight,
              sizeof(float) * conv4_3_in_channel * conv4_3_out_channel *
                  conv4_3_kernel_size * conv4_3_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv4_3_bias, conv4_3_bias, sizeof(float) * conv4_3_out_channel,
              cudaMemcpyHostToDevice);

  //////////BLOCK 5/////////////////////////////////
  cudaMemcpy(d_conv5_1_weight, conv5_1_weight,
             sizeof(float) * conv5_1_in_channel * conv5_1_out_channel *
                 conv5_1_kernel_size * conv5_1_kernel_size,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv5_1_bias, conv5_1_bias, sizeof(float) * conv5_1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv5_2_weight, conv5_2_weight,
              sizeof(float) * conv5_2_in_channel * conv5_2_out_channel *
                  conv5_2_kernel_size * conv5_2_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv5_2_bias, conv5_2_bias, sizeof(float) * conv5_2_out_channel,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv5_3_weight, conv5_3_weight,
              sizeof(float) * conv5_3_in_channel * conv5_3_out_channel *
                  conv5_3_kernel_size * conv5_3_kernel_size,
              cudaMemcpyHostToDevice);
  cudaMemcpy(d_conv5_3_bias, conv5_3_bias, sizeof(float) * conv5_3_out_channel,
              cudaMemcpyHostToDevice);


  cudaMemcpy(d_fc1_weight, fc1_weight,
             sizeof(float) * fc1_in_channel * fc1_out_channel,
             cudaMemcpyHostToDevice);
  cudaMemcpy(d_fc1_bias, fc1_bias, sizeof(float) * fc1_out_channel,
             cudaMemcpyHostToDevice);

  // copy input image
  size_t image_size = batch * input_size * input_size * input_channel;
  cudaMemcpy(d_image, image, image_size * sizeof(uint8_t),
             cudaMemcpyHostToDevice);
}

void vgg16_cuda::classify(int* predict, int batch) {
  // read logits back to cpu
  cudaMemcpy(output, d_output, sizeof(float) * output_size * batch,
             cudaMemcpyDeviceToHost);
  // Softmax
  softmax(output, predict, batch, output_size);
}

vgg16_cuda::~vgg16_cuda() {
  cudaFree(d_conv1_1_weight);   
  cudaFree(d_conv1_2_weight);   
  cudaFree(d_conv2_1_weight);   
  cudaFree(d_conv2_2_weight);  
  cudaFree(d_conv3_1_weight);   
  cudaFree(d_conv3_2_weight);   
  cudaFree(d_conv3_3_weight);   
  cudaFree(d_conv4_1_weight);   
  cudaFree(d_conv4_2_weight);   
  cudaFree(d_conv4_3_weight); 
  cudaFree(d_conv5_1_weight);   
  cudaFree(d_conv5_2_weight);   
  cudaFree(d_conv5_3_weight);   
 
  cudaFree(d_conv1_1_bias);   
  cudaFree(d_conv1_2_bias);   
  cudaFree(d_conv2_1_bias);   
  cudaFree(d_conv2_2_bias);  
  cudaFree(d_conv3_1_bias);   
  cudaFree(d_conv3_2_bias);   
  cudaFree(d_conv3_3_bias);   
  cudaFree(d_conv4_1_bias);   
  cudaFree(d_conv4_2_bias);   
  cudaFree(d_conv4_3_bias); 
  cudaFree(d_conv5_1_bias);   
  cudaFree(d_conv5_2_bias);   
  cudaFree(d_conv5_3_bias);   
   
  cudaFree(d_fc1_weight);     
  cudaFree(d_fc1_bias);        

  cudaFree(d_image);          
  cudaFree(d_input); 

  cudaFree(d_input_padded);          
  cudaFree(d_C1_1_feature_map); 
  cudaFree(d_C1_1_feature_map_padded); 
  cudaFree(d_C1_2_feature_map); 
  cudaFree(d_S1_feature_map); 

  cudaFree(d_S1_feature_map_padded); 
  cudaFree(d_C2_1_feature_map); 
  cudaFree(d_C2_1_feature_map_padded); 
  cudaFree(d_C2_2_feature_map); 
  cudaFree(d_S2_feature_map); 

  cudaFree(d_S2_feature_map_padded); 
  cudaFree(d_C3_1_feature_map); 
  cudaFree(d_C3_1_feature_map_padded); 
  cudaFree(d_C3_2_feature_map); 
  cudaFree(d_C3_2_feature_map_padded); 
  cudaFree(d_C3_3_feature_map); 
  cudaFree(d_S3_feature_map); 

  cudaFree(d_S3_feature_map_padded); 
  cudaFree(d_C4_1_feature_map); 
  cudaFree(d_C4_1_feature_map_padded); 
  cudaFree(d_C4_2_feature_map); 
  cudaFree(d_C4_2_feature_map_padded); 
  cudaFree(d_C4_3_feature_map); 
  cudaFree(d_S4_feature_map); 

  cudaFree(d_S4_feature_map_padded); 
  cudaFree(d_C5_1_feature_map); 
  cudaFree(d_C5_1_feature_map_padded); 
  cudaFree(d_C5_2_feature_map); 
  cudaFree(d_C5_2_feature_map_padded); 
  cudaFree(d_C5_3_feature_map); 
  cudaFree(d_S5_feature_map); 
 
  cudaFree(d_output);       
  cudaFree(d_predict_cuda);   
}
