"use strict";(self.webpackChunkmarket=self.webpackChunkmarket||[]).push([[963],{5836:(e,t,r)=>{r.d(t,{A:()=>a});r(5043);var s=r(4164),n=r(579);const a=e=>{let{children:t}=e;return(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(s.A,{}),(0,n.jsx)("div",{className:"bg-white my-5 w-full flex flex-col space-y-4 md:flex-row md:space-x-4 \r\nmd:space-y-0",children:(0,n.jsx)("main",{className:"bg-white md:w-2/3 lg:w-3/4 px-5 py-10",children:(0,n.jsx)("h1",{className:"text-2xl md:text-4xl",children:t})})})]})}},4164:(e,t,r)=>{r.d(t,{A:()=>o});var s=r(3003),n=r(5475);class a extends Error{}function l(e){let t=e.replace(/-/g,"+").replace(/_/g,"/");switch(t.length%4){case 0:break;case 2:t+="==";break;case 3:t+="=";break;default:throw new Error("base64 string is not of the correct length")}try{return function(e){return decodeURIComponent(atob(e).replace(/(.)/g,((e,t)=>{let r=t.charCodeAt(0).toString(16).toUpperCase();return r.length<2&&(r="0"+r),"%"+r})))}(t)}catch(r){return atob(t)}}a.prototype.name="InvalidTokenError";const c=r.p+"static/media/image.a743f0ce8b7615ff7da9.png";var i=r(579);const o=()=>{(0,s.d4)((e=>e.loginSlice));const e=localStorage.getItem("jwt"),t=!!e,r=t&&"SELLER"===function(e,t){if("string"!==typeof e)throw new a("Invalid token specified: must be a string");t||(t={});const r=!0===t.header?0:1,s=e.split(".")[r];if("string"!==typeof s)throw new a("Invalid token specified: missing part #".concat(r+1));let n;try{n=l(s)}catch(c){throw new a("Invalid token specified: invalid base64 for part #".concat(r+1," (").concat(c.message,")"))}try{return JSON.parse(n)}catch(c){throw new a("Invalid token specified: invalid json for part #".concat(r+1," (").concat(c.message,")"))}}(e).role;return(0,i.jsxs)("nav",{id:"navbar",className:"flex bg-blue-300",children:[(0,i.jsx)("div",{className:"w-4/5 bg-orange-400",children:(0,i.jsxs)("ul",{className:"flex p-4 text-white font-bold",children:[(0,i.jsx)("li",{className:"pr-6 text-2xl ",children:(0,i.jsx)(n.Link,{to:"/",children:(0,i.jsx)("img",{src:c,alt:"Logo",style:{maxHeight:"100px"}})})}),t?(0,i.jsxs)(i.Fragment,{children:[(0,i.jsx)("li",{className:"pr-6 text-3xl mt-10",children:(0,i.jsx)(n.Link,{to:"/carts/",children:"Cart"})}),(0,i.jsx)("li",{className:"pr-6 text-3xl mt-10",children:(0,i.jsx)(n.Link,{to:"/coupons/",children:"Coupons"})})]}):(0,i.jsx)(i.Fragment,{}),r?(0,i.jsx)(i.Fragment,{children:(0,i.jsx)("li",{className:"pr-6 text-3xl mt-10",children:(0,i.jsx)(n.Link,{to:"/store/",children:"Store"})})}):(0,i.jsx)(i.Fragment,{})]})}),(0,i.jsxs)("div",{className:"w-1/5 flex justify-end bg-orange-300 p-4 font-medium",children:[t?(0,i.jsx)("li",{className:"pr-6 text-3xl mt-10",children:(0,i.jsx)(n.Link,{to:"/member/mypage",children:"Mypage"})}):(0,i.jsx)(i.Fragment,{}),t?(0,i.jsx)("div",{className:"text-white text-3xl m-1 rounded mt-10",children:(0,i.jsx)(n.Link,{to:"/member/logout",children:"Logout"})}):(0,i.jsx)("div",{className:"text-white text-3xl m-1 rounded mt-10",children:(0,i.jsx)(n.Link,{to:"/member/login",children:"Login"})})]})]})}},8143:(e,t,r)=>{r.r(t),r.d(t,{default:()=>l});var s=r(3216),n=r(5836),a=(r(5043),r(579));const l=()=>(0,a.jsx)(n.A,{children:(0,a.jsx)(s.sv,{})})}}]);
//# sourceMappingURL=963.8e2fd1ab.chunk.js.map