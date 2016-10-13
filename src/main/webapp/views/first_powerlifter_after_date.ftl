<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<#--<h3>SimpleConnection test</h3>-->
<h4>Show first born powerlifter after ${params.date?number_to_date!"Неизвестно"}</h4>
<p>Data for the first powerlifter after ${params.date?number_to_date!"Неизвестно"}:</p>
<table border="1" width="100%">
    <thead>
    <tr>
        <th>Id</th>
        <th>Last name</th>
        <th>First name</th>
        <th>Sex</th>
        <th>Birthdate</th>
        <th>Birthplace</th>
    </tr>
    </thead>
    <tbody>
    <tr align="center">
        <td>${params.id!""}</td>
        <td>${params.lastName!""}</td>
        <td>${params.firstName!""}</td>
        <td>${params.sex!""}</td>
        <td>${params.birthdate!""}</td>
        <td>${params.birthplace!""}</td>
    </tr>
    </tbody>

</table>
</body>
</html>