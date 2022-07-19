interface AccessToken {
  accessToken: string;
}
const getters = {
  getAccessToken(module: any): AccessToken {
    return module.state.accessToken;
  },
};
export default getters;
