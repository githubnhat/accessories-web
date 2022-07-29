import axios from 'axios';

type Brand = {
  id: number;
  name: string;
  code: string;
};

type BrandResponse = {
  page: number;
  totalPages: number;
  totalItems: number;
  data: Array<Brand>;
};

type GetBrandResponse = {
  data: BrandResponse;
};

type SortRequest = {
  sortBy: string;
  sortDesc: boolean;
};

export async function getAllBrand(
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
    const { data, status } = await axios.post<GetBrandResponse>(`admin/brand/list`, dataForm, {
      withCredentials: true,
    });
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

export async function insertBrand(dataForm: object) {
  try {
    const path = 'admin/brand';
    const { data, status } = await axios.post<GetBrandResponse>(path, dataForm, {
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

// check unique brand name

type CheckUniqueBrandNameResponse = {
  data: boolean;
};

export async function checkUniqueBrandName(name: string) {
  try {
    const path = `admin/brand/exists/${name}`;
    const { data, status } = await axios.get<CheckUniqueBrandNameResponse>(path, {
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

// check unique brand code

type CheckUniqueBrandCodeResponse = {
  data: boolean;
};

export async function checkUniqueBrandCode(code: string) {
  try {
    const path = `admin/brand/existsCode/${code}`;
    const { data, status } = await axios.get<CheckUniqueBrandCodeResponse>(path, {
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

// delete brand by array id brand

type DeleteBrandResponse = {
  data: boolean;
};

export async function deleteBrands(ids: Array<number>) {
  try {
    const path = `admin/brand`;
    const { data, status } = await axios.delete<DeleteBrandResponse>(path, {
      data: ids,
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
