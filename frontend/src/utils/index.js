/**
 * input is a root array contains multi arrays inside
 * output is a array contains combination of each element inside each array in the root array
 * example: 
 * + input: 
  var array1=["A","B","C"];
  var array2=["1","2","3","4"];
 * + output: 
  [ [ 'A', '1' ],
    [ 'A', '2' ],
    [ 'A', '3' ],
    [ 'A', '4' ],
    [ 'B', '1' ],
    [ 'B', '2' ],
    [ 'B', '3' ],
    [ 'B', '4' ],
    [ 'C', '1' ],
    [ 'C', '2' ],
    [ 'C', '3' ],
    [ 'C', '4' ] ]
 * @param {*} param0 
 * @returns 
 */
export function cartesian([...args]) {
  var r = [],
    max = args.length - 1;
  function helper(arr, i) {
    for (var j = 0, l = args[i].length; j < l; j++) {
      var a = arr.slice(0); // clone arr
      a.push(args[i][j]);
      if (i == max) r.push(a);
      else helper(a, i + 1);
    }
  }
  helper([], 0);
  return r;
}

export function combineVariants(value) {
  if (value.length === 0) return [];
  const cartes = cartesian(value);

  const result = cartes.map((item) => ({
    productVariantName: item.reduce((accomulator, subItem) => accomulator + '-' + subItem),
    quantity: 1,
    price: 1000,
    isUse: true,
  }));
  return result;
}

export function removeAccents(str) {
  var AccentsMap = [
    'aàảãáạăằẳẵắặâầẩẫấậ',
    'AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬ',
    'dđ',
    'DĐ',
    'eèẻẽéẹêềểễếệ',
    'EÈẺẼÉẸÊỀỂỄẾỆ',
    'iìỉĩíị',
    'IÌỈĨÍỊ',
    'oòỏõóọôồổỗốộơờởỡớợ',
    'OÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢ',
    'uùủũúụưừửữứự',
    'UÙỦŨÚỤƯỪỬỮỨỰ',
    'yỳỷỹýỵ',
    'YỲỶỸÝỴ',
  ];
  str = str.trim();
  for (var i = 0; i < AccentsMap.length; i++) {
    var re = new RegExp('[' + AccentsMap[i].substr(1) + ']', 'g');
    var char = AccentsMap[i][0];
    str = str.replace(re, char);
  }
  return str;
}

/**
 *
 * @param {*} str
 * @returns seo code from name being transfer
 */
export function transNameToCode(str) {
  const result = removeAccents(str).toLowerCase().replaceAll(' ', '-');

  return result;
}

export function toDiscountPrice(str, discount){
  var result;
  if(str!== "Hết hàng"){
    var arr = str.split(" - ");
  var arrTemp=[];
  arr.forEach(element => {
    var price = element.slice(1).replaceAll('.','')*(100-discount)/100;
    arrTemp.push(price);
  });
  
  if(arrTemp.length==2){
    result = "₫" + arrTemp[0].toLocaleString('vi-VN') + " - " + "₫" + arrTemp[1].toLocaleString('vi-VN');
  } else if(arrTemp.length==1){
    result = "₫" + arrTemp[0].toLocaleString('vi-VN')
  }
  } else {
    result = str
  }
  return result;
}