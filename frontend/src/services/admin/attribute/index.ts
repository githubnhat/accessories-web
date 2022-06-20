import axios from 'axios';

const token = localStorage.getItem('accessToken');
axios.defaults.headers.common = { Authorization: `Bearer ${token}` };

type AttributeAndVariants = {
  id: number;
  attributeName: string;
  variantNames: Array<string>;
};

type AttributeAndVariantsResponse = {
  data: AttributeAndVariants;
};

type GetAttributeAndVariantsResponse = {
  data: Array<AttributeAndVariants>;
};

export async function getAllAttributes(path: string) {
  try {
    const { data, status } = await axios.get<GetAttributeAndVariantsResponse>(path);
    const mapData = data?.data.map(({ id, attributeName, variantNames}) => {
      let variantResult = null;
      if (variantNames.length > 0) {
        variantResult = variantNames.reduce((previousValue, currentValue) => {
          return previousValue + ', ' + currentValue;
        });
      }

      return {
        id: id,
        attributeName: attributeName,
        variantNames: variantResult,
      };
    });
    console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
    // 👇️ "response status is: 200"
    console.log('response status is: ', status);
    return mapData;
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

export async function addNewAttributes(path: string, dataForm: object) {
  try {
    const { data, status } = await axios.post<AttributeAndVariantsResponse>(path, dataForm);
      let variantResult = null;
      console.log(data?.data)
      if (data?.data != null && data?.data.variantNames.length > 0) {
        variantResult = data?.data.variantNames.reduce((previousValue, currentValue) => {
          return previousValue + ', ' + currentValue;
        });
        return {
          id: data?.data.id,
          attributeName: data?.data.attributeName,
          variantNames: variantResult,
        };
      }
      console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
    // 👇️ "response status is: 200"
    console.log('response status is: ', status);
      return null;
    
   
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
