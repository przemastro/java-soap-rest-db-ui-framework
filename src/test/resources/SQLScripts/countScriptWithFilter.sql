DECLARE @tableName nvarchar(50) = ?;
DECLARE @column nvarchar(50) = ?;
DECLARE @filterName nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'select count(1) from ' + @tableName + ' where ' + @column + ' = ' + @filterName;
EXEC (@query);