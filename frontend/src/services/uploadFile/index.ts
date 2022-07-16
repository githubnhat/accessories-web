import { getStorage, ref, uploadBytes, getDownloadURL } from 'firebase/storage';
export async function upLoadImg(files: Array<any>) {
  try {
    const images: string[] = [];
    const storage = getStorage();
    files.forEach(async (file) => {
      const storageRef = ref(storage, 'images/' + file.name);
      const img = await uploadBytes(storageRef, file);
      const src = await getDownloadURL(img.ref);
      images.push(src);
    });
    return images;
  } catch (error) {
    console.log('error rồi nè ', error);
  }
}
