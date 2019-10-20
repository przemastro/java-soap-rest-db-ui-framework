DECLARE @columnName nvarchar(50) = ?;
DECLARE @tableName nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'select top 1 '+@columnName+' from '+tableName;
EXEC (@query);