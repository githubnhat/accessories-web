/* eslint-disable */
const mutations = {
  addProductToCart(module: any, additionalItem: any) {
    let isAdd = false;
    module.state.cart = module.state.cart.map((item: any) => {
      if (item.name === additionalItem.name) {
        isAdd = true;
        return { ...item, quantity: ++item.quantity };
      }
      return item;
    });
    if (!isAdd) {
      module.state.cart = [...module.state.cart, additionalItem];
    }
  },
};
export default mutations;
