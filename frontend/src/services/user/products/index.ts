/* eslint-disable @typescript-eslint/no-explicit-any */
// import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';

type Product = {
  id: string;
  productName: string;
  description: string;
  originalPrice: string;
  originalQuantity: number;
  discount: string;
  imageLinks: Array<string>;
  attributeAndVariants: any;
  combinations: any;
  brandName: any;
  categoryName: string;
  createdDate: string;
  modifiedDate: string;
};

type GetProductsResponse = {
  data: Array<Product>;
  page: number;
  totalItems: number;
  totalPages: number;
};

// get home products

export async function getHomePageProduct() {
  try {
    const path = '/user/product/no-token/1/12';
    const { data, status } = await axios.get<GetProductsResponse>(path, {
      withCredentials: true,
    });
    console.log('response status is: ', status);
    return data?.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.log('error message: ', error.message);
      return error.message;
    } else {
      console.log('unexpected error: ', error);
      return 'An unexpected error occurred';
    }
  }
}
