import axios from 'axios'
type Account = {
    id: number,
    username: string,
    password: string,
    fullName: string,
    gmail: string,
    role: string
  };
  
  
  type AccountPageResponse = {
    page:number,
    totalPages: number,
    totalItems: number,
    data: Array<Account>;
  };
  
  type GetAccountResponse = {
    data: AccountPageResponse;
  };

  type SortRequest = {
    sortBy: string;
    sortDesc: boolean;
  };

  export async function getAllAccounts(pageNumber: number, itemsPerPage:number, sort: Array<SortRequest>) {
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
      const { data, status } = await axios.post<GetAccountResponse>(`/admin/account/list-accounts`,dataForm,{withCredentials: true});
    //   console.log("test",data?.data)
    //   console.log(JSON.stringify(data, null, 10));
    //   console.log('response status is: ', status);
      return data?.data;
    // return {page: 1,
    //     totalPages: 2,
    //     totalItems: 10,
    //     data: [{
    //     id: 1,
    //     username: 'minhnhat',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt43934@gmail.com',
    //     role: 'Admin',
    //   },
    //   {
    //     id: 2,
    //     username: 'minhnhat1',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt43943@gmail.com',
    //     role: 'User',
    //   },
    //   {
    //     id: 3,
    //     username: 'minhnhat2',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'Admin',
    //   },
    //   {
    //     id: 4,
    //     username: 'minhnhat3',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'User',
    //   },
    //   {
    //     id: 5,
    //     username: 'minhnhat2',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'Admin',
    //   },
    //   {
    //     id: 6,
    //     username: 'minhnhat3',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'User',
    //   },
    //   {
    //     id: 7,
    //     username: 'minhnhat2',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'Admin',
    //   },
    //   {
    //     id: 8,
    //     username: 'minhnhat3',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'User',
    //   },
    //   {
    //     id: 9,
    //     username: 'minhnhat2',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'Admin',
    //   },
    //   {
    //     id: 10,
    //     username: 'minhnhat3',
    //     password: 'sdsadhlsadjasda',
    //     fullName: 'Trần Quang Minh Nhật',
    //     gmail: 'nhatt44393@gmail.com',
    //     role: 'User',
    //   }]}
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
   
  // get all roles
  type Role ={
    id: number,
    roleName: string,
    roleCode: string,
  }

  type GetRoleResponse = {
    data: Array<Role>
  }
  export async function getAllRoles() {
    try {
      const { data, status } = await axios.get<GetRoleResponse>(`/admin/roles`,{withCredentials: true});
      console.log(data?.data)
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

  type AccountResponse ={
    data: Account
  }

  export async function addAccount(dataForm: object){
    try {
      const { data, status } = await axios.post<AccountResponse>(`/admin/account`, dataForm, {withCredentials: true});
      console.log(data?.data)
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

  export async function updateAccount(dataForm: object){
    try {
      const data = await axios.put<AccountResponse>(`/admin/account/update`, dataForm, {withCredentials: true});
      console.log(data?.data)
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

  type DeleteAccountResponse = {
    data: boolean;
  };

  export async function deleteAccount(ids: Array<number>) {
    try {
      const path = `admin/account`;
      const data = await axios.delete<DeleteAccountResponse>(path, {
        data: ids,
        withCredentials: true,
      });
      // console.log(JSON.stringify(data, null, 10));
  
      // 👇️ "response status is: 200"
      console.log('response status is: ', data);
  
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

  