import axios from 'axios';


type AttributeAndVariants = {
  id: number;
  attributeName: string;
  variantNames: Array<string>;
};

type AttributeAndVariantsResponse = {
  data: AttributeAndVariants;
};

type AttributeAndVariantsPageResponse = {
  page:number,
  totalPages: number,
  totalItems: number,
  data: Array<AttributeAndVariants>;
};

type GetAttributeAndVariantsResponse = {
  data: AttributeAndVariantsPageResponse;
};

export async function getAllAttributes(pageNumber: number, itemsPerPage:number) {
  try {
    const { data, status } = await axios.get<GetAttributeAndVariantsResponse>(`/admin/attribute/${pageNumber}/${itemsPerPage}`,{withCredentials: true});
    console.log("test",data?.data)
    const mapData = data?.data?.data.map(({ id, attributeName, variantNames}) => {
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
    return {
      page: data?.data.page,
      totalPages: data?.data?.totalPages,
      totalItems:data?.data?.totalItems,
      data: mapData
    };
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

export async function addNewAttributes(dataForm: object) {
  try {
    const { data, status } = await axios.post<AttributeAndVariantsResponse>(`/admin/attribute`, dataForm,{withCredentials: true});
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
