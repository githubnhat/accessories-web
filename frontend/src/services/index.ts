import router from '@/router';
import axios from 'axios';

type User = {
  accessToken: string;
};

type GetUsersResponse = {
  data: User;
};

export async function doLogin(path: string, dataForm: object) {
  try {
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

export default {};
