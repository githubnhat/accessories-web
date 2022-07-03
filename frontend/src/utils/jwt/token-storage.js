let ACCESS_TOKEN;
const ACCESS_TOKEN_LIFE = 3 * 24 * 60 * 60 * 1000 - 5000;

const decodeBase64 = (input) => {
  return decodeURIComponent(
    atob(input)
      .split("")
      .map(function(c) {
        return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
      })
      .join("")
  );
};

// const getInfoFromToken = (token) => {
//     if (token) {
//       const data = token.split(".")
//       if (data.length !== 3) return null
//       const result = JSON.parse(decodeBase64(data[1]))
//       return result;
//     }
//     return null;
//   };

const getUserInfo = (token) => {
  if (token) {
    const data = token.split(".")
    if (data.length !== 3) return null
    const result = JSON.parse(decodeBase64(data[1]))
    const userInfo = {
      id: result['sub'],    
      username: result['username'],
      fullname: result['fullName'],
    };
    return userInfo;
  }
  return null;
};

export default {
  ACCESS_TOKEN: ACCESS_TOKEN,
  ACCESS_TOKEN_LIFE: ACCESS_TOKEN_LIFE,
  getUserInfo: getUserInfo,
};
