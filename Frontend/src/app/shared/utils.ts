import { DomSanitizer } from '@angular/platform-browser';

export function showImg(
    sanitizer: DomSanitizer,
    picBytes: any,
    imageType: any,
    imgName: any
) {
    const byteString = window.atob(picBytes);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: imageType });
    
    const imgFile = new File([blob], imgName, { type: imageType });
    
    return sanitizer.bypassSecurityTrustUrl(
        window.URL.createObjectURL(imgFile)
      );
}