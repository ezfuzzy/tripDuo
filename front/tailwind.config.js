/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        tripDuoGreen: '#006622',  // 새로운 색상 추가
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms')
  ],
}