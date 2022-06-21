import axios from 'axios';
// axios.defaults.baseURL = 'http://localhost:1010/';

const token = localStorage.getItem('accessToken');
axios.defaults.headers.common = { Authorization: `Bearer ${token}` };

// get product attributes

type ProductAttribute = {
  id: number;
  attributeName: string;
};

type GetProductAttributeResponse = {
  data: Array<ProductAttribute>;
};

export async function getProductAttributes(path: string) {
  try {
    const { data, status } = await axios.get<GetProductAttributeResponse>(path);
    console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
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

// get product brands

type ProductBrand = {
  id: number;
  attributeName: string;
};

type GetProductBrandResponse = {
  data: Array<ProductBrand>;
};

export async function getProductBrands(path: string) {
  try {
    const { data, status } = await axios.get<GetProductBrandResponse>(path);
    console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
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
