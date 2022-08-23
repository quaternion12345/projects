const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  //npm run build 타겟 디렉토리
  // outputDir: "../../BE/utf/src/main/resources/static",

  //포트 다르기때문에 프록시 설정
  devServer: {
    proxy: {
      "/api": {
        // '/api' 로 들어오면 포트 8081(스프링 서버)로 보낸다
        target: "http://localhost:8080",
        changeOrigin: true, // cross origin 허용
      },
    },
  },
});
