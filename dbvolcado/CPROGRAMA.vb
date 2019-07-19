Imports MySql.Data.MySqlClient

Public Class CPROGRAMA
    Inherits CONEXION
    Dim conex, conex2 As MySqlConnection
    Dim Remota, LOCAL As String
    Dim A, B, C, D, E, F, G, H, I, J, K, L, CODPROG, FECHA, FECHAFIN, HORA, HORAFIN, LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO As String
    Dim FC, FCF As DateTime
    Dim comando, SQL As MySqlCommand
    Dim TTareaR, TTareaL, TProgR, TProgL, TDiaR, TDiaL, THoraR, THoraL, TValvR, TValvL As String
    Dim reader As MySql.Data.MySqlClient.MySqlDataReader

        
    Public Sub COPIAPROG(ByVal DATOS As DATOSCONEXION)
        Remota = "server=" & DATOS.DBHOSTR & ";" & "user id=" & DATOS.DBUSUARIOR & ";" & "password=" & DATOS.DBPASSR & ";" & "database=" & DATOS.DBDBR & ";" & "Port=" & DATOS.DBPUERTOR & ";"
        LOCAL = "server=" & DATOS.DBHOSTL & ";" & "user id=" & DATOS.DBUSUARIOL & ";" & "password=" & DATOS.DBPASSL & ";" & "database=" & DATOS.DBDBL & ";" & "Port=" & DATOS.DBPUERTOL & ";"
        conex = New MySqlConnection(REMOTA)
        conex2 = New MySqlConnection(LOCAL)

        TAREA(DATOS) 'COPIA TABLA TAREA  V02
        VALVULAS(DATOS) 'COPIA TABLA VALVULAS V13
        HORAS(DATOS) 'COPIA TABLA HORAS V12
        DIAS(DATOS) 'COPIA TABLA DIAS V11
        PROGRAMA(DATOS) 'COPIA TABLA PROGRAMA V10, AL MARCAR EL LEIDO DESAPARECEN LAS VISTAS DE LAS DEMAS TABLAS EN REMOTO.
    End Sub


    Public Sub TAREA(ByVal DATOS As DATOSCONEXION)
        TTareaR = "v02_tareaexec"
        TTareaL = "TAREA"

        'NO PUEDO HACER UN TRUNCATE PORQUE BORRARIA LAS EJECUCIONES MANUALES.
        'Try
        ' conex2.Open()
        ' SQL = New MySqlCommand("TRUNCATE TABLE " & TTareaL, conex2)
        ' SQL.ExecuteNonQuery()
        ' conex2.Close()
        ' Catch TRUNCATE As Exception
        ' conex2.Close()
        ' End Try

        conex.Open()
        comando = New MySqlCommand("select * from " & TTareaR & " where CODPROG= " & DATOS.IDESTACION, conex)
        reader = comando.ExecuteReader()

        While reader.Read
            Try
                CODPROG = reader.GetString(1)
                B = CODPROG
                If CODPROG = DATOS.IDESTACION Then
                    A = reader.GetString(0)

                    'EL IDPROGRAMA LO INSERTO EN LA CLASE CONEXION
                    'Try
                    ' conex2.Open()
                    ' SQL = New MySqlCommand("INSERT INTO " & TTareaL & " (IDTAREA)values ( " & A & ") ;", conex2)
                    ' SQL.ExecuteNonQuery()
                    ' conex2.Close()
                    ' Catch IDDuplicado As Exception
                    'conex2.Close()
                    'End Try

                    If reader.IsDBNull(2) Then  'CODELECVALV
                        C = 0
                    Else
                        C = reader.GetString(2)
                    End If
                    D = reader.GetString(3) 'IDDSTAREA 

                    If reader.IsDBNull(4) Then 'FCEXEC
                        FC = #1/1/2001 12:00:01 AM#
                    Else
                        FC = reader.GetDateTime(4)
                    End If
                    FECHA = FC.Year & "-" & FC.Month & "-" & FC.Day
                    HORA = FC.Hour & ":" & FC.Minute & ":" & FC.Second
                    If reader.IsDBNull(5) Then ' VALOR
                        F = 0
                    Else
                        F = reader.GetInt16(5)
                    End If
                    If reader.IsDBNull(6) Then ' CODTAREA
                        G = 0
                    Else
                        G = reader.GetString(6)
                    End If
                    If reader.IsDBNull(7) Then ' DSTAREA
                        H = ""
                    Else
                        H = reader.GetString(7)
                    End If

                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("update " & TTareaL & " set CODPROG=" & B & ", CODELECVALV=" & C & ", IDDSTAREA=" & D & ", FCEXEC=""" & FECHA & " " & HORA & """,  VALOR=" & F & ", CODTAREA=""" & G & """, DSTAREA=""" & H & """ where IDTAREA = " & A, conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try
                End If
            Catch e As TimeoutException
            Catch ex As MySqlException
                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            End Try

        End While
        reader.Close()
        conex.Close()
    End Sub
    
    ''Aki copia la programacion
    Public Sub PROGRAMA(ByVal DATOS As DATOSCONEXION)
        Dim conexleido As MySqlConnection
        conexleido = New MySqlConnection(Remota)
        TProgR = "v10_programaexec"
        TProgL = "PROGRAMA"


        'Try
        ' conex2.Open()
        ' SQL = New MySqlCommand("TRUNCATE TABLE " & TProgL, conex2)
        ' SQL.ExecuteNonQuery()
        ' conex2.Close()
        ' Catch TRUNCATE As Exception
        ' conex2.Close()
        ' End Try

        conex.Open()
        comando = New MySqlCommand("select * from " & TProgR & " where CODPROG= " & DATOS.IDESTACION, conex)
        reader = comando.ExecuteReader()

        While reader.Read
            Try
                CODPROG = reader.GetString(6)
                B = CODPROG
                If CODPROG = DATOS.IDESTACION Then
                    A = reader.GetString(0)  'IDPROGRAMA

                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("INSERT INTO " & TProgL & " (IDPROGRAMA)values ( " & A & ") ;", conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch IDDuplicado As Exception
                        conex2.Close()
                    End Try
                    If reader.IsDBNull(1) Then  'CODPROGRAMA
                        C = ""
                    Else
                        C = reader.GetString(1)
                    End If
                    If reader.IsDBNull(2) Then ' DSPROGRAMA
                        D = ""
                    Else
                        D = reader.GetString(2)
                    End If
                    If reader.IsDBNull(3) Then 'FCINICIO
                        FC = #1/1/2001#
                    Else
                        FC = reader.GetDateTime(3)
                        FECHA = FC.Year & "-" & FC.Month & "-" & FC.Day
                    End If
                    If reader.IsDBNull(4) Then 'FCFIN
                        FCF = #1/1/2001#
                    Else
                        FCF = reader.GetDateTime(4)
                        FECHAFIN = FCF.Year & "-" & FCF.Month & "-" & FCF.Day
                    End If
                    If reader.IsDBNull(5) Then 'ACTIVO
                        E = ""
                    Else
                        E = reader.GetString(5)
                    End If
                    If reader.IsDBNull(7) Then 'TIPO
                        F = ""
                    Else
                        F = reader.GetString(7)
                    End If
                    If reader.IsDBNull(8) Then 'LUNES
                        LUNES = 0
                    Else
                        LUNES = reader.GetInt16(8)
                    End If
                    If reader.IsDBNull(9) Then ' MARTES
                        MARTES = 0
                    Else
                        MARTES = reader.GetInt16(9)
                    End If
                    If reader.IsDBNull(10) Then 'MIERCOLES
                        MIERCOLES = 0
                    Else
                        MIERCOLES = reader.GetInt16(10)
                    End If
                    If reader.IsDBNull(11) Then ' JUEVES
                        JUEVES = 0
                    Else
                        JUEVES = reader.GetInt16(11)
                    End If
                    If reader.IsDBNull(12) Then 'VIERNES
                        VIERNES = 0
                    Else
                        VIERNES = reader.GetInt16(12)
                    End If
                    If reader.IsDBNull(13) Then ' SABADO
                        SABADO = 0
                    Else
                        SABADO = reader.GetInt16(13)
                    End If
                    If reader.IsDBNull(14) Then 'DOMINGO
                        DOMINGO = 0
                    Else
                        DOMINGO = reader.GetInt16(14)
                    End If
                    If reader.IsDBNull(15) Then 'MODO
                        G = 0
                    Else
                        G = reader.GetString(15)
                    End If
                    If reader.IsDBNull(16) Then 'MODOINI
                        H = 0
                    Else
                        H = reader.GetString(16)
                    End If
                    If reader.IsDBNull(17) Then 'PBLOQUE
                        I = 0
                    Else
                        I = reader.GetInt16(17)
                    End If
                    If reader.IsDBNull(18) Then 'CUOTA
                        J = 0
                    Else
                        J = reader.GetInt16(18)
                    End If
                    If reader.IsDBNull(19) Then 'LEIDO
                        K = 0
                    Else
                        K = reader.GetString(19)
                    End If

                    If reader.IsDBNull(20) Then ' ENMARCHA
                        L = 0
                    Else
                        L = reader.GetString(20)
                    End If


                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("update " & TProgL & " set CODPROGRAMA=""" & C & """, DSPROGRAMA=""" & D & """, FCINICIO=""" & FECHA & """,  FCFIN=""" & FECHAFIN & """, ACTIVO=""" & E & """,CODPROG=" & B & ", TIPO=""" & F & """, DIAL=" & LUNES & ",DIAM=" & MARTES & ",DIAX=" & MIERCOLES & ",DIAJ=" & JUEVES & ",DIAV=" & VIERNES & ",DIAS=" & SABADO & ",DIAD=" & DOMINGO & ", MODO=""" & G & """,MODOINI=""" & H & """,PBLOQUE=" & I & ",CUOTA=" & J & ",LEIDO=""" & K & """,ENMARCHA=""" & L & """ where IDPROGRAMA = " & A, conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try

                    Try
                        conexleido.Open()
                        SQL = New MySqlCommand("update " & TProgR & " set LEIDO=""S"" where IDPROGRAMA = " & A, conexleido)
                        SQL.ExecuteNonQuery()
                        conexleido.Close()
                    Catch TRUNCATE As Exception
                        conexleido.Close()
                    End Try
                End If
            Catch e As TimeoutException
            Catch ex As MySqlException
                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            End Try

        End While
        reader.Close()
        conex.Close()
    End Sub
    Public Sub VALVULAS(ByVal DATOS As DATOSCONEXION)
        TValvR = "v13_progvalvexec"
        TValvL = "VALVULAS"

        'Try
        ' conex2.Open()
        ' SQL = New MySqlCommand("TRUNCATE TABLE " & TValvL, conex2)
        ' SQL.ExecuteNonQuery()
        ' conex2.Close()
        ' Catch TRUNCATE As Exception
        ' conex2.Close()
        ' End Try


        conex.Open()
        comando = New MySqlCommand("select * from " & TValvR, conex)
        reader = comando.ExecuteReader()

        While reader.Read
            Try
                CODPROG = reader.GetString(0)
                B = CODPROG
                If CODPROG = DATOS.IDESTACION Then
                    C = reader.GetInt16(1) 'IDPROGVALV
                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("INSERT INTO " & TValvL & " (IDVALVULAS,IDPROGVALV)values ( " & C & "," & C & ") ;", conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch IDDuplicado As Exception
                        conex2.Close()
                    End Try

                    If reader.IsDBNull(2) Then  'IDPROGRAMA
                        D = 0
                    Else
                        D = reader.GetInt16(2)
                    End If
                    If reader.IsDBNull(3) Then ' CODELECVALV
                        E = 0
                    Else
                        E = reader.GetString(3)
                    End If
                    If reader.IsDBNull(4) Then ' DURACION
                        F = 0
                    Else
                        F = reader.GetInt16(4)
                    End If
                    If reader.IsDBNull(5) Then ' BLOQUE
                        G = 0
                    Else
                        G = reader.GetInt16(5)
                    End If


                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("update " & TValvL & " set CODPROG=" & CODPROG & ",  IDPROGRAMA=""" & D & """,CODELECVALV=" & E & ",  DURACION=" & F & ", BLOQUE=" & G & " where IDVALVULAS = " & C, conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try
                End If
            Catch e As TimeoutException
            Catch ex As MySqlException
                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            End Try

        End While
        reader.Close()
        conex.Close()
    End Sub
    Public Sub HORAS(ByVal DATOS As DATOSCONEXION)
        THoraR = "v12_proghorasexec"
        THoraL = "HORAS"

        'Try
        ' conex2.Open()
        ' SQL = New MySqlCommand("TRUNCATE TABLE " & THoraL, conex2)
        ' SQL.ExecuteNonQuery()
        ' conex2.Close()
        ' Catch TRUNCATE As Exception
        ' conex2.Close()
        ' End Try

        conex.Open()
        comando = New MySqlCommand("select * from " & THoraR & " where CODPROG= " & DATOS.IDESTACION, conex)
        reader = comando.ExecuteReader()

        While reader.Read
            Try
                CODPROG = reader.GetString(0)
                B = CODPROG
                If CODPROG = DATOS.IDESTACION Then
                    C = reader.GetInt16(1) 'IDPROGHORAS
                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("INSERT INTO " & THoraL & " (IDHORAS,IDPROGHORAS)values ( " & C & "," & C & ") ;", conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch IDDuplicado As Exception
                        conex2.Close()
                    End Try

                    If reader.IsDBNull(2) Then  'IDPROGRAMA
                        D = 0
                    Else
                        D = reader.GetInt16(2)
                    End If
                    If reader.IsDBNull(3) Then ' HRINICIO

                        HORA = 0
                    Else
                        HORA = reader.GetString(3)
                    End If
                    'HORA = FC.Hour & ":" & FC.Minute & ":" & FC.Second

                    If reader.IsDBNull(4) Then ' HRFIN
                        HORAFIN = 0
                    Else
                        HORAFIN = reader.GetString(4)
                    End If
                    'HORAFIN = FC.Hour & ":" & FC.Minute & ":" & FC.Second



                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("update " & THoraL & " set CODPROG=" & CODPROG & ",  IDPROGRAMA=" & D & ",HRINICIO=""" & HORA & """,  HRFIN=""" & HORAFIN & """ where IDHORAS =" & C, conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try
                End If
            Catch e As TimeoutException
            Catch ex As MySqlException
                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            End Try

        End While
        reader.Close()
        conex.Close()
    End Sub
    Public Sub DIAS(ByVal DATOS As DATOSCONEXION)
        TDiaR = "v11_progdiasexec"
        TDiaL = "DIAS"

        'Try
        ' conex2.Open()
        ' SQL = New MySqlCommand("TRUNCATE TABLE " & TDiaL, conex2)
        ' SQL.ExecuteNonQuery()
        ' conex2.Close()
        ' Catch TRUNCATE As Exception
        ' conex2.Close()
        ' End Try

        conex.Open()
        comando = New MySqlCommand("select * from " & TDiaR, conex)
        reader = comando.ExecuteReader()

        While reader.Read
            Try
                CODPROG = reader.GetString(0)
                B = CODPROG
                If CODPROG = DATOS.IDESTACION Then
                    C = reader.GetInt16(1) 'IDPROGDIAS
                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("INSERT INTO " & TDiaL & " (IDDIAS,IDPROGDIAS)values ( " & C & "," & C & ") ;", conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch IDDuplicado As Exception
                        conex2.Close()
                    End Try

                    If reader.IsDBNull(2) Then  'IDPROGRAMA
                        D = 0
                    Else
                        D = reader.GetInt16(2)
                    End If
                    If reader.IsDBNull(3) Then ' FECHA
                        FC = #1/1/2001#
                    Else
                        FC = reader.GetDateTime(3)
                        FECHA = FC.Year & "-" & FC.Month & "-" & FC.Day
                    End If
                    Try
                        conex2.Open()
                        SQL = New MySqlCommand("update " & TDiaL & " set CODPROG=" & CODPROG & ",  IDPROGRAMA=" & D & ",  FECHA=""" & FECHA & """ where IDDIAS =" & C, conex2)
                        SQL.ExecuteNonQuery()
                        conex2.Close()
                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try
                End If
            Catch e As TimeoutException
            Catch ex As MySqlException
                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            End Try

        End While
        reader.Close()
        conex.Close()
    End Sub

End Class
