import axios from 'axios';
type SortRequest = {
    sortBy: string;
    sortDesc: boolean;
  };

  type Order = {
    id: string;
    address: string;
    phone: string;
    status: string;
    insertDate: string;
    totalBill: string;
    customerName: string;
    orderItems: Array<OrderItem>;
  };
  
  type OrderItem = {
    id: string;
    productId: string;
    price: string;
    productCombination: string;
    quantity: string;
    discount: string;
    brand: string;
    category: string;
    imageLink: string

  };
  
  type GetOrderResponse = {
    data: Order;
  };
  type GetOrderPageResponse = {
    page: number;
    totalPages: number;
    totalItems: number;
    data: Array<Order>;
  };
  
  export async function getAllOrder(
    pageNumber: number,
    itemsPerPage: number,
    sort: Array<SortRequest>,
  ) {
    try {
      let dataForm = null;
      if (sort[0].sortBy != null) {
        dataForm = {
          page: pageNumber,
          limit: itemsPerPage,
          sort: sort,
        };
      } else {
        dataForm = {
          page: pageNumber,
          limit: itemsPerPage,
          sort: null,
        };
      }
      const { data, status } = await axios.post<GetOrderPageResponse>(`admin/order/list-orders`, dataForm, {
        withCredentials: true,
      });
      console.log('response status is: ', status);
      return data;
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

  export async function getInforOrder(orderId: number) {
    try {
      const path = `/user/info/orders/${orderId}`;
      const { data, status } = await axios.get<GetOrderResponse>(path, {
        withCredentials: true,
      });
      // console.log(JSON.stringify(data, null, 10));
  
      // 👇️ "response status is: 200"
      console.log('response status is: ', status);
  
      return { data, status };
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

  export async function updateOrder(dataForm: object) {
    try {
      const path = `/admin/order`;
      const { data, status } = await axios.put<GetOrderResponse>(path, dataForm,{
        withCredentials: true,
      });
      // console.log(JSON.stringify(data, null, 10));
  
      // 👇️ "response status is: 200"
      console.log('response status is: ', status);
  
      return { data, status };
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
  
  