// 삭제 클릭 시 removeForm 전송

document.querySelector(".move").addEventListener("click", (e) => {
  e.preventDefault();

  document.querySelector("#removeForm").submit();
});
