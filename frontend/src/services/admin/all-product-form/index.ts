import axios from 'axios';

type Product = {
    id: number;
    productName: string;
    description: string;
    originalPrice: string;
    originalQuantity: number;
    discount: number;
    imageLinks: Array<string>;
    attributes: null;
    combinations: null;
    brandName: string;
    categoryName: string;
  };
  
  type ProductResponse = {
    page:number,
    totalPages: number,
    totalItems: number,
    data: Array<Product>;
  };
  
  type GetProductResponse = {
    data: ProductResponse;
  };
export async function getAllProducts(pageNumber: number, itemsPerPage:number) {
    try {
      const { data, status } = await axios
      .get<GetProductResponse>(`admin/product/${pageNumber}/${itemsPerPage}`,{withCredentials: true});
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

  type DeleteProducyResponse = {
    data: boolean;
  };
  export async function deleteProducts(ids: Array<number>) {
    try {
      const { data, status } = await axios
      .delete<DeleteProducyResponse>(`admin/product`,{
        data: ids,
        withCredentials: true
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