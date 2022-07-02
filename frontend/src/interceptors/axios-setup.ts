import axios, { AxiosRequestConfig } from "axios";

axios.defaults.baseURL ='http://localhost:8081/api/v1';

    axios.interceptors.request.use(
      (config: AxiosRequestConfig) => {
        const accessToken = localStorage.getItem('accessToken')
        if (!config.headers) {
          config.headers = {}
        }
        if(accessToken){
          config.headers.Authorization = `Bearer ${accessToken}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    axios.interceptors.response.use(
      (res) => {
        return res;
      },
      async (err) => {
        const originalConfig = err.config;
        if (originalConfig.url !== "/auth/login" && err.response) {
          // Access Token was expired
          if (err.response.status === 403 && !originalConfig._retry) {
            originalConfig._retry = true;
            try {
              const {data, status} = await axios.get('/auth/refresh-token', {withCredentials: true})
              if(status===200){
                // axios.defaults.headers.common = { Authorization: `Bearer ${data?.data?.accessToken}` };
                localStorage.setItem('accessToken', data?.data?.accessToken);
                // axios.defaults.headers.common = { Authorization: `Bearer ${localStorage.getItem('accessToken')}` };
              }
              return axios(originalConfig);
            } catch (_error) {
              return Promise.reject(_error);
            }
          }
        }
        return Promise.reject(err);
      }
    );


// let refresh = false;
// axios.interceptors.request.use(
//     (config) => {
//         const token = localStorage.getItem('accessToken');
//         if (token) {
//             axios.defaults.headers.common = { Authorization: `Bearer ${token}` };
//         }
//         return config
//     },
//     (error) => {
//         Promise.reject(error)
//     }
// )  
// axios.interceptors.response.use(resp => resp
// , async error => {
//     // const originalConfig = error.config
//     console.log("check",refresh)
//     if(error.response.status === 403 && !refresh){
//         refresh=true;
//         // console.log("ss",error.config._retry)
//         // const {status, data} = await refreshToken('/auth/refresh-token');
//         localStorage.setItem('accessToken', '');
//         const {status, data} = await axios.get('/auth/refresh-token', {withCredentials: true})
//         console.log(status)
        // if(status===200){
        //     // axios.defaults.headers.common = { Authorization: `Bearer ${data?.data?.accessToken}` };
        //     localStorage.setItem('accessToken', data?.data?.accessToken);
        //     axios.defaults.headers.common = { Authorization: `Bearer ${localStorage.getItem('accessToken')}` };
//             return axios(error.config);
//         }
//     }
//     refresh = false;
//     return Promise.reject(error);
// });



// const refreshToken = (url) => {
//     return new Promise(resolve => {
//         setTimeout(() => {
//          resolve(axios.get(url, {withCredentials: true}));
//         }, 1000);
//       });
//     };
   