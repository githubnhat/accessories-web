import router from '@/router';
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8081/api/v1';

type User = {
  accessToken: string;
};

type GetUsersResponse = {
  data: User;
};

// login into system

export async function doLogin(dataForm: object) {
  try {
    const path = '/auth/login';
    const { data, status } = await axios.post<GetUsersResponse>(path, dataForm, {withCredentials: true});
    // console.log(JSON.stringify(data, null, 10));
    if(status===200){
      localStorage.setItem('accessToken', data?.data?.accessToken || '');
    }

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
    const path = '/auth/register';

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

// verify account by otp

type OTP = {
  accessToken: string;
};

type GetVerifyOTPResponse = {
  data: OTP;
};

export async function verifyOTP(dataForm: object) {
  try {
    const path = '/auth/confirm';

    const { data, status } = await axios.post<GetVerifyOTPResponse>(path, dataForm);
    // console.log(JSON.stringify(data, null, 10));
    if (status === 200) {
      localStorage.setItem('accessToken', data?.data?.accessToken || '');
      router.push('/');
    } else {
      alert('OTP không đúng, vui lòng kiểm tra lại!');
    }
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

// refresh token
// type AccesstokenRefresh = {
//   accessToken: string;
// };

// type GetAccesstokenRefreshResponse = {
//   data: AccesstokenRefresh;
// };

export async function refreshToken() {

    axios.defaults.headers.common = { Authorization: `` };
    
  
}


export default {};

