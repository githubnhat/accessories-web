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
  if (value.length === 0) return false;
  const cartes = cartesian(value);

  const result = cartes.map((item) => ({
    productVariantName: item.reduce((accomulator, subItem) => accomulator + '-' + subItem),
    quantity: 1,
    price: 1000,
    isUse: true,
  }));
  return result;
}
