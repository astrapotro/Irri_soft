Imports MySql.Data.MySqlClient

Public Class CONEXION
    Inherits IRRIGESTDB


    Public Function FunDB(ByVal DATOS As DATOSCONEXION)

	
        Dim ROWR As T02
        ROWR = New T02()
        Dim ROWL As TABEXE
        ROWL = New TABEXE()
        Dim conexion, conexion2, conexion3 As MySqlConnection
        Dim Remota, LOCAL As String
        Dim comando, SQL As MySqlCommand
        Remota = "server=" & DATOS.DBHOSTR & ";" & "user id=" & DATOS.DBUSUARIOR & ";" & "password=" & DATOS.DBPASSR & ";" & "database=" & DATOS.DBDBR & ";" & "Port=" & DATOS.DBPUERTOR & ";"
        LOCAL = "server=" & DATOS.DBHOSTL & ";" & "user id=" & DATOS.DBUSUARIOL & ";" & "password=" & DATOS.DBPASSL & ";" & "database=" & DATOS.DBDBL & ";" & "Port=" & DATOS.DBPUERTOL & ";"
        conexion = New MySqlConnection(Remota)
        conexion2 = New MySqlConnection(LOCAL)
        conexion3 = New MySqlConnection(Remota)
        Dim tabla1 As String
        tabla1 = "tarea" 'tabla local de instrucciones


        Try

            conexion.Open() 'Abrimos la conexión a la remota y comprobamos que no hay error



            Dim reader As MySql.Data.MySqlClient.MySqlDataReader
            comando = New MySqlCommand("select * from " & DATOS.DBTABLA & " WHERE CODPROG= " & DATOS.IDESTACION, conexion)
            reader = comando.ExecuteReader()
            IRRIGESTDB.LSTATE.Text = "CONECTADO CON " & DATOS.DBHOSTR

            While reader.Read
                If reader.IsDBNull(1) Then
                    'DB1.Items.Add(".")
                    'No hay datos !
                Else
                    ROWL.IDTAREA = reader.GetString(0)
                    Try
                        Try
                            conexion2.Open()
                            SQL = New MySqlCommand("INSERT INTO " & tabla1 & " (IDTAREA)values ( " & ROWL.IDTAREA & ") ;", conexion2)
                            SQL.ExecuteNonQuery()
                            conexion2.Close()
                        Catch e As TimeoutException
                            conexion2.Close()
                        Catch IDDuplicado As Exception
                            conexion2.Close()
                        End Try

                        ROWR.CODPROG = reader.GetString(1) 'EL ENTERO INDICA EL NUMERO DE COLUMNA DE LA DB
                        If ROWR.CODPROG = DATOS.IDESTACION Then
                            If reader.IsDBNull(4) Then 'no hay fecha
                                ROWR.FCEXEC = #1/1/2001#
                            Else
                                ROWR.FCEXEC = reader.GetDateTime(4)
                            End If
                            'Aqui meto en la local la fecha y la hora de la tarea
                            ROWL.FCEXEC = ROWR.FCEXEC.Year & "-" & ROWR.FCEXEC.Month & "-" & ROWR.FCEXEC.Day
                            ROWL.HREXEC = ROWR.FCEXEC.Hour & ":" & ROWR.FCEXEC.Minute & ":" & ROWR.FCEXEC.Second

                            If reader.IsDBNull(2) Then
                                ROWL.CODELECVALV = 0
                            Else
                                ROWL.CODELECVALV = reader.GetString(2)
                            End If
							
							'Que hago con la electroválvula
                            ROWL.IDDSTAREA = reader.GetString(3) 'con 1 abre,2 cierra,3 indica programa | Preguntar a jaime???.
                            If ROWL.IDDSTAREA = 3 Then
                                CPROGRAMA.COPIAPROG(DATOS)   'Copia la programacion
                            End If
                            ROWL.CODPROG = reader.GetString(1)
                            If reader.IsDBNull(5) Then
                                ROWL.VALOR = 0
                            Else
                                ROWL.VALOR = reader.GetString(5)
                            End If


                            Try
                                conexion2.Open()
                                'Aqui actualiza a la local
                                SQL = New MySqlCommand("update " & tabla1 & " set CODPROG=" & ROWL.CODPROG & ", CODELECVALV=" & ROWL.CODELECVALV & ", IDDSTAREA=" & ROWL.IDDSTAREA & ", FCEXEC=""" & ROWL.FCEXEC & " " & ROWL.HREXEC & """,  VALOR=" & ROWL.VALOR & " where IDTAREA = " & ROWL.IDTAREA & " ;", conexion2)
                                SQL.ExecuteNonQuery()
                                conexion2.Close()
								
								'Aqui borra cuando debería borrar irrisoft
                                conexion3.Open()
                                SQL = New MySqlCommand("delete from " & DATOS.DBTABLA & " where IDTAREAEXEC=" & ROWL.IDTAREA & " ;", conexion3)
                                SQL.ExecuteNonQuery()
                                conexion3.Close()
                            Catch e As TimeoutException
                            Catch ex As MySqlException
                                MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                            End Try
                        End If

                    Catch e As TimeoutException
                    Catch ex As MySqlException
                        MessageBox.Show("No se ha podido grabar la instruccion" & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
                    End Try

                End If
            End While
            reader.Close()
            conexion.Close()
        Catch e As TimeoutException
            IRRIGESTDB.LSTATE.Text = "ERROR TIMEOUT REINTENTO EN 10s"
            Sleep(600)
        Catch ex As MySqlException
            'Si hubiese error en la conexión mostramos el texto de la descripción
            'IRRIGESTDB.Timer1.Enabled = False
            'IRRIGESTDB.LSTATE.Text = "SISTEMA DETENIDO POR ERROR EN CONEXION CON DB REMOTA"
            'IRRIGESTDB.BSTART.Text = "START"
            'MessageBox.Show("No se ha podido realizar la conexion a la base de datos." & Chr(13) & Chr(10) & ex.Message & Chr(13) & Chr(10) & ex.ErrorCode)
            IRRIGESTDB.LSTATE.Text = "REINTENTO DE CONEXION CON DB REMOTA EN 10s"
            Sleep(600)
            'Close() ' cierra programa
        End Try
    End Function


    Private Sub InitializeComponent()
        Me.SuspendLayout()
        '
        'CONEXION
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.ClientSize = New System.Drawing.Size(344, 150)
        Me.Name = "CONEXION"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Declare Sub Sleep Lib "kernel32" (ByVal dwMilliseconds As Long)
End Class
