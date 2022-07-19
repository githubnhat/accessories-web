/* eslint-disable */
const mutations = {
  setAccessToken(module: any, value: any) {
    module.state.accessToken = value;
  },
  resetAccessToken(module: any) {
    module.state.accessToken = undefined;
  },
};
export default mutations;
