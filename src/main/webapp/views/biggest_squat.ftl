<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h3>SimpleConnection test</h3>
<h4>Show biggest exercise for powerlifter</h4>
<p>Biggest Squat for Power500:</p>
<table border="1" width="100%">
    <thead>
    <tr>
        <th>Last name</th>
        <th>First name</th>
        <th>Sex</th>
        <th>Birthday</th>
        <th>Result in kg</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>${params.lastName!"Неизвестно"}</td>
        <td>${params.firstName!"Неизвестно"}</td>
        <td>${params.sex!"Неизвестно"}</td>
        <td>${params.birthday!"Неизвестно"}</td>
        <td>${params.resultInKg!"Неизвестно"}</td>
    </tr>
    </tbody>

</table>
</body>
</html>