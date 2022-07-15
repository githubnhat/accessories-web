import axios from 'axios';

type Category = {
  id: number;
  name: string;
  code: string;
};

type CategoryResponse = {
  page: number;
  totalPages: number;
  totalItems: number;
  data: Array<Category>;
};

type GetCategoryResponse = {
  data: CategoryResponse;
};
export async function getAllCategory() {
  try {
    const { data, status } = await axios.get<GetCategoryResponse>('admin/category', {
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

export async function insertCategory(dataForm: object) {
  try {
    const path = 'admin/category';
    const { data, status } = await axios.post<GetCategoryResponse>(path, dataForm, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));

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

// check unique product name

type CheckUniqueCategoryCodeResponse = {
  data: boolean;
};

export async function checkUniqueCategoryCode(code: string) {
  try {
    const path = `admin/category/exists/${code}`;
    const { data, status } = await axios.get<CheckUniqueCategoryCodeResponse>(path, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));

    // 👇️ "response status is: 200"
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
