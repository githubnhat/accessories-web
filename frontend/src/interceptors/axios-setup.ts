import router from '@/router';
import axios, { AxiosRequestConfig } from "axios";
import store from '../store'

axios.defaults.baseURL ='http://localhost:8081/api/v1';



export function setup() {
    axios.interceptors.request.use(
      (config: AxiosRequestConfig) => {
        const accessToken = store.getters['_accessToken/getAccessToken'];
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
          if (err.response.status === 401 && !originalConfig._retry) {
            originalConfig._retry = true;
            try {
              const {data, status} = await axios.get('/auth/refresh-token', {withCredentials: true})
              if(status===200){
                store.commit('_accessToken/setAccessToken', data?.data?.accessToken);
              } else if(status===403){
                store.commit('_accessToken/resetAccessToken');
                router.push('/login')
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
}
   