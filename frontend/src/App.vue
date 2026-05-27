<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body, #app {
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
}
.el-button--primary {
  background-color: #2C3E50;
  border-color: #2C3E50;
}
.el-button--primary:hover,
.el-button--primary:focus {
  background-color: #3D5167;
  border-color: #3D5167;
}
.el-button--primary.is-plain {
  color: #2C3E50;
  border-color: #2C3E50;
  background: transparent;
}
.el-button--primary.is-plain:hover {
  color: #fff;
  background-color: #2C3E50;
  border-color: #2C3E50;
}
.el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #2C3E50;
}
.el-menu-item.is-active {
  color: #C9A96E !important;
}
.el-tag--primary {
  background-color: rgba(44,62,80,0.1);
  border-color: rgba(44,62,80,0.2);
  color: #2C3E50;
}
</style>
