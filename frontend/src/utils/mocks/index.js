export const VARIANTS = [
  {
    id: -1,
    attributeName: 'Thêm thuộc tính mới',
    variantNames: [],
  },
  {
    id: 0,
    attributeName: 'Mặc định',
    variantNames: [],
  },
];

export const USER_MENU = [
  {
    action: 'mdi-account-tie',
    active: true,
    items: [{ title: 'Informations' }, { title: 'Orders' }],
    title: 'Profile',
  },
];
export const ADMIN_MENU = [
  {
    action: 'mdi-package-variant-closed',
    active: true,
    items: [{ title: 'All orders' }],
    title: 'Order',
  },

  {
    action: 'mdi-watermark',
    items: [{ title: 'All brands' }],
    title: 'Brand',
  },
  {
    action: 'mdi-label-multiple-outline',
    items: [{ title: 'All categories' }],
    title: 'Category',
  },
  {
    action: 'mdi-wallet',
    items: [
      { title: 'All products' },
      { title: 'Add product' },
      { title: 'All attribute product' },
    ],
    title: 'Product',
  },
  {
    action: 'mdi-account-multiple',
    items: [{ title: 'All accounts' }],
    title: 'Account',
  },
];

export const SELECT_ITEMS = [
  {
    title: 'Thêm',
    value: true,
  },
  {
    title: 'Không thêm',
    value: false,
  },
];

export const SLIDE_LIST = [
  'image/slide1.webp',
  'image/slide2.webp',
  'image/slide3.webp',
  'image/slide4.webp',
  'image/slide5.webp',
];
