import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1/auth';

type User = {
  accessToken: string;
};

type GetUsersResponse = {
  data: User;
};

// login into system

export async function doLogin(dataForm: object) {
  try {
    const path = '/login';
    const { data, status } = await axios.post<GetUsersResponse>(path, dataForm);
    // console.log(JSON.stringify(data, null, 10));
    localStorage.setItem('accessToken', data?.data?.accessToken || '');
    router.push('/');
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

// register new account

type Register = {
  id: number;
  username: string;
  password: string;
  fullName: string;
  gmail: string;
  roles: string;
  createdBy: string;
  createdDate: string;
  modifiedBy: string;
  modifiedDate: string;
};

type GetRegisterResponse = {
  data: Register;
};

export async function doRegister(dataForm: object) {
  try {
    const path = '/register';

    const { data, status } = await axios.post<GetRegisterResponse>(path, dataForm);
    // console.log(JSON.stringify(data, null, 10));
    // localStorage.setItem('accessToken', data?.data?.accessToken || '');
    // if (status === 200) {
    //   router.push('/');
    // }
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

export default {};
