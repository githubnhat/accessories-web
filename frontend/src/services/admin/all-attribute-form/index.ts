import axios from 'axios';

type AttributeAndVariants = {
  attributeId: number;
  attributeName: string;
  variantNames: Array<string>;
};

type AttributeAndVariantsResponse = {
  data: AttributeAndVariants;
};

type AttributeAndVariantsPageResponse = {
  page: number;
  totalPages: number;
  totalItems: number;
  data: Array<AttributeAndVariants>;
};

type SortRequest = {
  sortBy: string;
  sortDesc: boolean;
};

type GetAttributeAndVariantsResponse = {
  data: AttributeAndVariantsPageResponse;
};

export async function getAllAttributes(
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
    const { data, status } = await axios.post<GetAttributeAndVariantsResponse>(
      `/admin/attribute/list-attributes`,
      dataForm,
      { withCredentials: true },
    );
    const mapData = data?.data?.data.map(({ attributeId, attributeName, variantNames }) => {
      let variantResult = null;
      if (variantNames.length > 0) {
        variantResult = variantNames.reduce((previousValue, currentValue) => {
          return previousValue + ', ' + currentValue;
        });
      }

      return {
        id: attributeId,
        attributeName: attributeName,
        variantNames: variantResult,
      };
    });
    // console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
    // 👇️ "response status is: 200"
    // console.log('response status is: ', status);
    return {
      page: data?.data.page,
      totalPages: data?.data?.totalPages,
      totalItems: data?.data?.totalItems,
      data: mapData,
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
    const { data, status } = await axios.post<AttributeAndVariantsResponse>(
      `/admin/attribute`,
      dataForm,
      { withCredentials: true },
    );
    let variantResult = null;
    console.log(data?.data);
    if (data?.data != null && data?.data.variantNames.length > 0) {
      variantResult = data?.data.variantNames.reduce((previousValue, currentValue) => {
        return previousValue + ', ' + currentValue;
      });
      return {
        id: data?.data.attributeId,
        attributeName: data?.data.attributeName,
        variantNames: variantResult,
      };
    }
    // console.log(JSON.stringify(data, null, 10));
    // console.log('axios', data);
    // 👇️ "response status is: 200"
    // console.log('response status is: ', status);
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

// check unique brand code

type CheckUniqueAttributeNameResponse = {
  data: boolean;
};

export async function checkUniqueAttributeName(name: string, id: number) {
  try {
    const path = `admin/attribute/exists/${id}/${name}`;
    const { data, status } = await axios.get<CheckUniqueAttributeNameResponse>(path, {
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
type UpdateAttributeAndVariants = {
  data: AttributeAndVariants;
};
export async function updateAttributes(dataForm: object) {
  try {
    const path = `admin/attribute/update`;
    const { data, status } = await axios.put<UpdateAttributeAndVariants>(path, dataForm, {
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

type DeleteAttributeNameResponse = {
  data: boolean;
};
export async function deleteAttributes(ids: Array<number>) {
  try {
    const path = `admin/attribute`;
    const { data, status } = await axios.delete<DeleteAttributeNameResponse>(path, {
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
