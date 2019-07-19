Imports System.IO

Public Class IRRIGESTDB

    Dim DATOSDB As DATOSCONEXION = New DATOSCONEXION



    Private Sub IRRIGESTDB_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load
        Dim LINEA As String
        Dim ERCNX As String = "FALTAN LOS SIGUIENTES DATOS:" & Chr(13) & Chr(10)
        Dim ERCNXIN As Integer = 0
        


        Try
            Dim CARGA As StreamReader
            
            'La ruta la cambio para linux'
            'CARGA = New StreamReader("c:\DB.txt")'
            
            CARGA = New StreamReader("/home/mikel/DB.txt")
            
            
            DATOSDB.DBTABLA = "t02_tareaexec" 'NOMBRE DE LA TABLA A ATACAR
            DATOSDB.DBPUERTOR = "3306"  ' PUERTO POR DEFECTO MYSQL
            DATOSDB.DBPUERTOL = "3306"
            LINEA = CARGA.ReadLine()
            Do While Not (LINEA Is Nothing)

                If LINEA.Contains("ID:") Then
                    DATOSDB.IDESTACION = LINEA.Remove(0, 3)
                ElseIf LINEA.Contains("HOSTR:") Then
                    DATOSDB.DBHOSTR = LINEA.Remove(0, 6)
                ElseIf LINEA.Contains("PUERTOR:") Then
                    DATOSDB.DBPUERTOR = LINEA.Remove(0, 8)
                ElseIf LINEA.Contains("DBR:") Then
                    DATOSDB.DBDBR = LINEA.Remove(0, 4)
                ElseIf LINEA.Contains("USUARIOR:") Then
                    DATOSDB.DBUSUARIOR = LINEA.Remove(0, 9)
                ElseIf LINEA.Contains("PASSR:") Then
                    DATOSDB.DBPASSR = LINEA.Remove(0, 6)
                ElseIf LINEA.Contains("TIEMPO:") Then
                    DATOSDB.DBTIEMPO = LINEA.Remove(0, 7)
                ElseIf LINEA.Contains("HOSTL:") Then
                    DATOSDB.DBHOSTL = LINEA.Remove(0, 6)
                ElseIf LINEA.Contains("PUERTOL:") Then
                    DATOSDB.DBPUERTOL = LINEA.Remove(0, 8)
                ElseIf LINEA.Contains("DBL:") Then
                    DATOSDB.DBDBL = LINEA.Remove(0, 4)
                ElseIf LINEA.Contains("USUARIOL:") Then
                    DATOSDB.DBUSUARIOL = LINEA.Remove(0, 9)
                ElseIf LINEA.Contains("PASSL:") Then
                    DATOSDB.DBPASSL = LINEA.Remove(0, 6)
                ElseIf LINEA.Contains("AUTOINI:") Then
                    DATOSDB.AUTOINI = LINEA.Remove(0, 8)
                End If
                LINEA = CARGA.ReadLine()
            Loop
            CARGA.Close()

            If DATOSDB.DBHOSTR = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL HOST DE LA BASE DE DATOS REMOTA"
                ERCNXIN += 1
            End If
            If DATOSDB.DBUSUARIOR = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL USUARIO DE CONEXIÓN REMOTA"
                ERCNXIN += 1
            End If
            If DATOSDB.DBPASSR = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL PASSWORD DE LA BASE DE DATOS REMOTA"
                ERCNXIN += 1
            End If
            If DATOSDB.DBDBR = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL NOMBRE DE LA BASE DE DATOS REMOTA"
                ERCNXIN += 1
            End If
            If DATOSDB.DBTIEMPO = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL TIEMPO DE RECONEXIÓN CON LA BASE DE DATOS"
                ERCNXIN += 1
            End If

            If DATOSDB.DBHOSTL = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL HOST DE LA BASE DE DATOS LOCAL"
                ERCNXIN += 1
            End If

            If DATOSDB.DBDBL = Nothing Then
                ERCNX += Chr(13) & Chr(10) & "* DEFINA EL NOMBRE DE LA BASE DE DATOS LOCAL"
                ERCNXIN += 1
            End If





            If ERCNXIN < 1 Then
                'MsgBox("ARCHIVO DE CONFIGURACION CARGADO CORRECTAMENTE")
                If DATOSDB.AUTOINI = "Checked" Then
                    Timer1.Enabled = True
                    BSTART.Text = "DETENER"
                    LSTATE.Text = "CONECTANDO..."
                End If
            Else
                MsgBox(ERCNX + "" & Chr(13) & Chr(10) & Chr(13) & Chr(10) & "CONFIGURE LOS PARAMETROS REQUERIDOS Y REINICIE EL SISTEMA")
                BSTART.Enabled = False
                BRESET.Enabled = False

            End If

            If DATOSDB.AUTOINI = "SI" Then
                Timer1.Enabled = True
                BSTART.Text = "DETENER"
                LSTATE.Text = "CONECTANDO..."
            ElseIf DATOSDB.AUTOINI = "NO" Then
                Timer1.Enabled = False
                BSTART.Text = "CONECTAR"
                LSTATE.Text = "SISTEMA DETENIDO"
            End If

        Catch ex As Exception

            Timer1.Enabled = False
            LSTATE.Text = "SISTEMA DETENIDO"
            MsgBox("ARCHIVO DE CONFIGURACION NO ENCONTRADO, CONFIGURE LOS DISPOSITIVOS Y REINICIE EL PROGRAMA")
        End Try

    End Sub

    Private Sub BCONF_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BCONF.Click
        CONF.Visible = True
        
	
    End Sub


    Private Sub BSTART_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BSTART.Click
        If BSTART.Text = "CONECTAR" Then
            BSTART.Text = "DETENER"
            LSTATE.Text = "CONECTANDO..."
            Timer1.Enabled = True
        Else
            BSTART.Text = "CONECTAR"
            Timer1.Enabled = False
            LSTATE.Text = "SISTEMA DETENIDO"
        End If
    End Sub
 

    Private Sub Timer1_Tick(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Timer1.Tick
        CONEXION.FunDB(DATOSDB)
        Timer1.Interval = DATOSDB.DBTIEMPO * 1000
    End Sub

    Private Sub BRESET_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BRESET.Click
        Timer1.Enabled = False
        Timer1.Enabled = True
    End Sub

End Class
