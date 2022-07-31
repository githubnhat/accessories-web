import axios from 'axios';
axios.defaults.baseURL = 'http://localhost:8081/api/v1';

// get infor product for update

export async function getInforProduct(productId: number) {
  try {
    const path = `/admin/product/${productId}`;

    const { data, status } = await axios.get<GetProductResponse>(path, {
      withCredentials: true,
    });
    console.log(JSON.stringify(data, null, 10));
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

// get attributes available for new product

type ProductAttribute = {
  id: number;
  attributeName: string;
};

type GetProductAttributeResponse = {
  data: Array<ProductAttribute>;
};

export async function getProductAttributes() {
  try {
    const path = '/admin/attribute/listVariants';

    const { data, status } = await axios.get<GetProductAttributeResponse>(path, {
      withCredentials: true,
    });
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

type Brand = {
  id: number;
  name: string;
  code: string;
};

type GetBrandsResponse = {
  data: Array<Brand>;
};

export async function getBrands() {
  try {
    const path = '/admin/brand';

    const { data, status } = await axios.get<GetBrandsResponse>(path, { withCredentials: true });
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

type Category = {
  id: number;
  name: string;
  code: string;
};

type GetCategoriesResponse = {
  data: Array<Category>;
};

export async function getCategories() {
  try {
    const path = '/admin/category';

    const { data, status } = await axios.get<GetCategoriesResponse>(path, {
      withCredentials: true,
    });
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

// add product service

type Combination = {
  productVariantName: string;
  price: number;
  quantity: number;
};

type Product = {
  productName: string;
  description: string;
  originalPrice: string;
  originalQuantity: number;
  discount: number;
  imageLinks: Array<string>;
  attributes: Array<ProductAttribute>;
  combinations: Array<Combination>;
  brandName: string;
  categoryName: string;
};

type GetProductResponse = {
  data: Product;
};

export async function insertProduct(dataForm: object) {
  try {
    const path = '/admin/product/insert';

    const { data, status } = await axios.post<GetProductResponse>(path, dataForm, {
      withCredentials: true,
    });
    // console.log(JSON.stringify(data, null, 10));
    // localStorage.setItem('accessToken', data?.data?.accessToken || '');
    // 👇️ "response status is: 200"
    console.log('response status is: ', status);
    if (status === 200) alert('Thêm sản phẩm thành công');
    else alert('Thêm sản phẩm không thành công');

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

// check unique product name when update

type CheckUniqueProductNameResponse = {
  data: boolean;
};

export async function checkUniqueProductName(productName: string) {
  try {
    const path = `/admin/product/exists/${productName}`;
    const { data, status, request } = await axios.get<CheckUniqueProductNameResponse>(path, {
      withCredentials: true,
    });
    console.log(JSON.stringify(data, null, 10));
    console.log('request', request);

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

export default {};
