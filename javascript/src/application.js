function formatDate(dateString) {
  var d = dateString.split('-');
  var day = d[2].split("T");
  return d[1]+'/'+day[0]+'/'+d[0];
}