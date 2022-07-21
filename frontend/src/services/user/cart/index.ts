import axios from 'axios';


type Cart = {
    id: number,
    productId: number,
    productName: string,
    thumnail: string,
    productVariantName: string,
    price: string,
    discount: number,
    quantity: number
}
type GetCartResponse = {
    data: Cart;
}
export async function addToCart(dataForm: object){
    try {
        const path =`/user/cart/add-to-cart`;
        const { data, status } = await axios.post<GetCartResponse>(path, dataForm, {
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

export async function updateCart(dataForm: object){
  try {
      const path =`/user/cart/update`;
      const { data, status } = await axios.post<GetCartResponse>(path, dataForm, {
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

export async function getListCart(){
    try {
        const path =`/user/cart/list-cart`;
        const { data, status } = await axios.get<GetCartResponse>(path, {
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

type DeleteCartResponse ={
data: boolean,
}

export async function deleteCart(ids: Array<number>){
    try {
        const path =`/user/cart/delete`;
        const { data, status } = await axios.delete<DeleteCartResponse>(path, {
            data: ids,
            withCredentials: true,
        })
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