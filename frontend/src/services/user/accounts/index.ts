/* eslint-disable @typescript-eslint/no-explicit-any */
// import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';

type Address = {
  id: number;
  address: string;
  isMainAddress: boolean;
  phone: string;
};

type GetAddressesResponse = {
  data: Array<Address>;
};

// get all addresses of the current user

export async function getAddresses() {
  try {
    const path = '/user/info/addresses';
    const { data, status } = await axios.get<GetAddressesResponse>(path, {
      withCredentials: true,
    });
    
    // router.push('/');
    // 👇️ response status is: 200


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

// add new address

type GetAddressResponse = {
  data: Address;
};

export async function insertAddress(dataForm: any) {
  try {
    const path = '/user/info/address';
    const { data, status } = await axios.post<GetAddressResponse>(path, dataForm, {
      withCredentials: true,
    });


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
