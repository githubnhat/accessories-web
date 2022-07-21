// import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';

type Address = {
  id: number;
  address: string;
  isMainAddress: boolean;
  phone: string;
};

type GetUsersResponse = {
  data: Address;
};

// login into system

export async function getAddresses() {
  try {
    const path = '/user/info/addresses';
    const { data, status } = await axios.get<GetUsersResponse>(path, {
      withCredentials: true,
    });
    console.log(JSON.stringify(data, null, 10));
    // router.push('/');
    // 👇️ response status is: 200
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
