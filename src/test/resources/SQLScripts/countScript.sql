DECLARE @tableName nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'select count(1) from ' + @tableName;
EXEC (@query);