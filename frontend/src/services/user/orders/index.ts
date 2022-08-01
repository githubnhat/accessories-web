// import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';

type Order = {
  id: string;
  address: string;
  phone: string;
  status: string;
  insertDate: string;
  totalBill: string;
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
  imageLink: string;
  category: string;
};

type GetOrderResponse = {
  data: Order;
};

// login into system

export async function insertOrder(dataForm: object) {
  try {
    const path = '/user/info/order';
    const { data, status } = await axios.post<GetOrderResponse>(path, dataForm, {
      withCredentials: true,
    });
    console.log(JSON.stringify(data, null, 10));

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

//get all order
type GetOrdersResponse = {
  page:number,
  totalPages: number,
  totalItems: number,
  data: Array<Order>;
};

export async function getAllOrders(page: number, itemPerPage: number) {
  try {
    const path = `/user/info/orders/page/${page}/limit/${itemPerPage}`;
    const { data, status } = await axios.get<GetOrdersResponse>(path, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));

    // 👇️ "response status is: 200"
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

// get infor of a order by id

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
