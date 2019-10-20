DECLARE @procedureName nvarchar(50) = ?;
DECLARE @query nvarchar(100) = 'exec '+@procedureName;
EXEC (@query);