import { DomSanitizer } from '@angular/platform-browser';

// const monthNames = ["January", "February", "March", "April", "May", "June",
//   "July", "August", "September", "October", "November", "December"
// ];

const monthNames = ["Tháng 01", "Tháng 02", "Tháng 03", "Tháng 04", "Tháng 05", "Tháng 06",
  "Tháng 07", "Tháng 08", "Tháng 09", "Tháng 10", "Tháng 11", "Tháng 12"
];

const dayNames = ["Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy"];

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

export function customFormattedDate(isoString: string) {
    const date = new Date(isoString);

    const datee = date.getDate();
    let dateStr: string;
    if (datee<10) {
      dateStr = "0"+datee;
    } else {
      dateStr = datee+'';
    }

    const monthIndex = date.getMonth(); // 0-indexed (January is 0)
    const year = date.getFullYear();
    const day = date.getDay();

    const formattedDate = `${dayNames[day]}, ${dateStr} ${monthNames[monthIndex]}, ${year}`;
    
    return formattedDate; // date.toString()
}

export function msToTime(ms: number) {
  const seconds = Math.floor(ms / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const months = Math.floor(days / 30); // Approximation (30 days/month)

  const remainingDays = days % 30;
  const remainingHours = hours % 24;
  const remainingMinutes = minutes % 60;
  const remainingSeconds = seconds % 60;

  return {
    months,
    days: remainingDays,
    hours: remainingHours,
    minutes: remainingMinutes,
    seconds: remainingSeconds
  };
}

export function formatNumber(num: number) {
  return num.toLocaleString('de-DE');
}