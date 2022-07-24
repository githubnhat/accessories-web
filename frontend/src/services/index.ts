// import router from '@/router';
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
    const { data, status } = await axios.post<GetUsersResponse>(path, dataForm, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));
    // if (status === 200) {
    //   localStorage.setItem('accessToken', data?.data?.accessToken || '');
    // }

    // router.push('/');
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

//login admin
export async function doAdminLogin(dataForm: object) {
  try {
    const path = '/auth/admin/login';
    const { data, status } = await axios.post<GetUsersResponse>(path, dataForm, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));
    // if (status === 200) {
    //   localStorage.setItem('accessToken', data?.data?.accessToken || '');
    // }

    // router.push('/');
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
      // router.push('/');
    } else {
      alert('OTP không đúng, vui lòng kiểm tra lại!');
    }
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

export async function doLogout() {
  try {
    const path = '/auth/logout';

    const { status } = await axios.get<GetRegisterResponse>(path, { withCredentials: true });

    console.log('response status is: ', status);

    return status;
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

// get brands for display to customer

type Brand = {
  id: number;
  name: string;
  code: string;
};

type GetBrandsResponse = {
  data: Array<Brand>;
};

export async function getBrandList() {
  try {
    const path = '/user/brand/no-token';

    const { data, status } = await axios.get<GetBrandsResponse>(path, { withCredentials: true });
    // console.log(JSON.stringify(data, null, 10));

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

type Category = {
  id: number;
  name: string;
  code: string;
};

type GetCategoriesResponse = {
  data: Array<Category>;
};

export async function getCategoryList() {
  try {
    const path = '/user/category/no-token';

    const { data, status } = await axios.get<GetCategoriesResponse>(path, {
      withCredentials: true,
    });
    console.log(JSON.stringify(data, null, 10));

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

export async function refreshToken() {
  axios.defaults.headers.common = { Authorization: `` };
}

export default {};
