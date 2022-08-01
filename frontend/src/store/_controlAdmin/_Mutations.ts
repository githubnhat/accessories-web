/* eslint-disable */
const mutations = {
  setController(module: any, value: any) {
    module.state.controller = value;
  },
  resetController(module: any) {
    module.state.controller = undefined;
  },
};
export default mutations;
