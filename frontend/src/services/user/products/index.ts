/* eslint-disable @typescript-eslint/no-explicit-any */
// import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';
type AttributeAndVariants ={
  attributeId: number,
  attributeName: string,
  variantNames: VariantNames,
}

type VariantNames = {
  variantNames: Array<string>
}

type Product = {
  id: string;
  productName: string;
  description: string;
  originalPrice: string;
  originalQuantity: number;
  discount: string;
  imageLinks: Array<string>;
  attributeAndVariants: Array<AttributeAndVariants>;
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
    const path = '/user/product/no-token/page/1/limit/12 ';
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
type GetProductDetailResponse = {
  data: Product
};
export async function getProductDetail(id: number) {
  try {
    const path =`/user/product/no-token/${id}`;
    const { data, status } = await axios.get<GetProductDetailResponse>(path, {
      withCredentials: true,
    });
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

type ProductCombination = {
        price: string,
        quantity: number
}

type GetProductCombinationResponse = {
  data: ProductCombination
}

export async function getProductCombination (dataForm: object) {
  try {
    const path =`/user/product/no-token/productCombination`;
    const { data, status } = await axios.post<GetProductCombinationResponse>(path, dataForm, {
      withCredentials: true,
    });
    console.log('combination: ', data?.data);
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
